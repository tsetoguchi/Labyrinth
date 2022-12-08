package json;

import static model.board.Direction.DOWN;
import static model.board.Direction.LEFT;
import static model.board.Direction.RIGHT;
import static model.board.Direction.UP;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.Position;
import model.board.Board;
import model.board.Direction;
import model.board.Gem;
import model.board.IBoard;
import model.board.Tile;
import model.board.Treasure;
import model.state.IState;
import model.state.PlayerAvatar;
import model.state.SlideAndInsertRecord;
import model.state.State;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import referee.Move;

/**
 * Represents a json deserializer for labyrinth
 */
public class JsonDeserializer {

  private static final Map<String, Color> stringToColor;
  static Map<String, Set<Direction>> symbolToDirection = new HashMap<>();
  static Map<Set<Direction>, String> directionToSymbol = new HashMap<>();

  static {
    symbolToDirection.put("│", Set.of(UP, DOWN));
    symbolToDirection.put("─", Set.of(RIGHT, LEFT));
    symbolToDirection.put("┐", Set.of(DOWN, LEFT));
    symbolToDirection.put("└", Set.of(UP, RIGHT));
    symbolToDirection.put("┌", Set.of(RIGHT, DOWN));
    symbolToDirection.put("┘", Set.of(UP, LEFT));
    symbolToDirection.put("┬", Set.of(RIGHT, DOWN, LEFT));
    symbolToDirection.put("├", Set.of(UP, RIGHT, DOWN));
    symbolToDirection.put("┴", Set.of(UP, RIGHT, LEFT));
    symbolToDirection.put("┤", Set.of(UP, DOWN, LEFT));
    symbolToDirection.put("┼", Set.of(UP, RIGHT, DOWN, LEFT));

    for (Map.Entry<String, Set<Direction>> entry : symbolToDirection.entrySet()) {
      directionToSymbol.put(entry.getValue(), entry.getKey());
    }

    stringToColor = new HashMap<>();
    stringToColor.put("purple", new Color(218, 112, 214));
    stringToColor.put("orange", Color.ORANGE);
    stringToColor.put("pink", Color.PINK);
    stringToColor.put("red", Color.RED);
    stringToColor.put("blue", Color.BLUE);
    stringToColor.put("green", Color.GREEN);
    stringToColor.put("yellow", Color.YELLOW);
    stringToColor.put("white", Color.WHITE);
    stringToColor.put("black", Color.BLACK);
  }

  public static IState state(JSONObject game) throws JSONException {
    IBoard board = board(game.getJSONObject("board"), game.getJSONObject("spare"));
    List<PlayerAvatar> playerAvatars = playerAvatars(game.getJSONArray("plmt"));

    if(!game.isNull("last")){
      SlideAndInsertRecord previousTurn = lastTurn(game.getJSONArray("last"));
      return new State(board, playerAvatars, previousTurn);
    }
    return new State(board, playerAvatars);
  }

  private static SlideAndInsertRecord lastTurn(JSONArray last) throws JSONException {
    int index = last.getInt(0);
    Direction direction = Direction.valueOf(last.getString(1));
    return new SlideAndInsertRecord(direction, index, 0);
  }

  private static List<PlayerAvatar> playerAvatars(JSONArray plmt) throws JSONException {
    List<PlayerAvatar> players = new ArrayList<>();
    for(int i=0; i<plmt.length(); i++){
      players.add(playerAvatar(plmt.getJSONObject(i)));
    }
    return players;
  }

  private static PlayerAvatar playerAvatar(JSONObject jsonPlayer) throws JSONException {
    Color color = stringToColor(jsonPlayer.getString("color"));
    Position current = coordinate(jsonPlayer.getJSONObject("current"));
    Position home = coordinate(jsonPlayer.getJSONObject("home"));
    return new PlayerAvatar(color, home, current);
  }

  public static Color stringToColor(String color) {
    if (stringToColor.containsKey(color)) {
      return stringToColor.get(color);
    }
    return Color.decode("#" + color);
  }

  public static Position coordinate(JSONObject jsonPosition) throws JSONException {
    int row = jsonPosition.getInt("row#");
    int col = jsonPosition.getInt("column#");
    return new Position(row, col);
  }


  public static IBoard board(JSONObject boardJSON, JSONObject spareJSON) throws JSONException {
    Tile spare = spare(spareJSON);

    JSONArray rowsJSON = boardJSON.getJSONArray("connectors");
    JSONArray rowsGemJSON = boardJSON.getJSONArray("treasures");

    int height = rowsJSON.length();
    int width = rowsJSON.getJSONArray(0).length();

    Tile[][] tiles = new Tile[height][width];

    for(int r=0; r<height; r++){
      JSONArray rowJSON = rowsJSON.getJSONArray(r);
      JSONArray rowGemJSON = rowsGemJSON.getJSONArray(r);
      for(int c=0; c<width; c++){
        String connector = rowJSON.getString(c);
        Set<Direction> pathwayConnections = symbolToDirection.get(connector);
        JSONArray treasureJSON = rowGemJSON.getJSONArray(0);
        Gem g1 = Gem.getGem(treasureJSON.getString(0));
        Gem g2 = Gem.getGem(treasureJSON.getString(1));
//        Gem g1 = null;
//        Gem g2 = null;
        Treasure treasure = new Treasure(g1, g2);
        tiles[r][c] = new Tile(pathwayConnections, treasure);
      }
    }
    return new Board(width, height, tiles, spare);
  }

  public static Tile spare(JSONObject spareJSON) throws JSONException {
    String connector = spareJSON.getString("tilekey");
    Set<Direction> pathwayConnections = symbolToDirection.get(connector);
//    Gem gem1 = Gem.valueOf(spareJSON.getString("1-image"));
//    Gem gem2 = Gem.valueOf(spareJSON.getString("2-image"));
    Gem gem1 = null;
    Gem gem2 = null;
    Treasure treasure = new Treasure(gem1, gem2);
    return new Tile(pathwayConnections, treasure);
  }


  public static List<Position> goals(JSONObject jsonGame) throws JSONException {
    JSONArray jsonPlmt = jsonGame.getJSONArray("plmt");
    List<Position> goals = new ArrayList<>();
    for(int i=0; i<jsonPlmt.length(); i++){
      JSONObject playerJSON = jsonPlmt.getJSONObject(i);
      Position goal = coordinate(playerJSON.getJSONObject("goto"));
      goals.add(goal);
    }

    if(!jsonGame.isNull("goals")){
      JSONArray jsonGoals = jsonGame.getJSONArray("goals");
      for(int i=0; i<jsonGame.length(); i++){
        JSONObject jsonPosition = jsonGoals.getJSONObject(i);
        Position p = JsonDeserializer.coordinate(jsonPosition);
        goals.add(p);
      }
    }

    return goals;
  }


  public static Move move(JSONArray moveJSON) throws JSONException {
    int index = moveJSON.getInt(0);
    Direction direction = Direction.valueOf(moveJSON.getString(1));
    int rotations = degreesToRotations(moveJSON.getInt(2));
    Position moveTo = JsonDeserializer.coordinate(moveJSON.getJSONObject(3));
    return new Move(direction, index, rotations, moveTo);
  }

  public static int degreesToRotations(int degrees) {
    int counterClockwiseRotations = degrees / 90;
    return (4 - counterClockwiseRotations) % 4;
  }


}
