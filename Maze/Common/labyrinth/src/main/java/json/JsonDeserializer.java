package json;

import model.Position;
import model.board.Board;
import model.board.Direction;
import model.board.Gem;
import model.board.IBoard;
import model.board.Tile;
import model.board.Treasure;
import model.state.IState;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.state.PlayerAvatar;
import model.state.SlideAndInsertRecord;
import model.state.State;
import player.IPlayer;
import player.Player;

import static model.board.Direction.DOWN;
import static model.board.Direction.LEFT;
import static model.board.Direction.RIGHT;
import static model.board.Direction.UP;

public class JsonDeserializer {

  private static final Map<String, Color> colors;
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

    colors = new HashMap<>();
    colors.put("purple", new Color(218, 112, 214));
    colors.put("orange", Color.ORANGE);
    colors.put("pink", Color.PINK);
    colors.put("red", Color.RED);
    colors.put("blue", Color.BLUE);
    colors.put("green", Color.GREEN);
    colors.put("yellow", Color.YELLOW);
    colors.put("white", Color.WHITE);
    colors.put("black", Color.BLACK);
  }

  public static IState jsonToState(JSONObject game) throws JSONException {
    IBoard board = jsonToBoard(game.getJSONObject("board"), game.getJSONObject("spare"));
    List<PlayerAvatar> playerAvatars = jsonToPlayerAvatars(game.getJSONArray("plmt"));

    if(!game.isNull("last")){
      SlideAndInsertRecord previousTurn = jsonToLastTurn(game.getJSONArray("last"));
      return new State(board, playerAvatars, previousTurn);
    }
    return new State(board, playerAvatars);
  }

  private static SlideAndInsertRecord jsonToLastTurn(JSONArray last) throws JSONException {
    int index = last.getInt(0);
    Direction direction = Direction.valueOf(last.getString(1));
    return new SlideAndInsertRecord(direction, index, 0);
  }

  private static List<PlayerAvatar> jsonToPlayerAvatars(JSONArray plmt) throws JSONException {
    List<PlayerAvatar> players = new ArrayList<>();
    for(int i=0; i<plmt.length(); i++){
      players.add(jsonToPlayerAvatar(plmt.getJSONObject(i)));
    }
    return players;
  }

  private static PlayerAvatar jsonToPlayerAvatar(JSONObject jsonPlayer) throws JSONException {
    Color color = colors.get(jsonPlayer.getString("color"));
    Position current = jsonToPosition(jsonPlayer.getJSONObject("current"));
    Position home = jsonToPosition(jsonPlayer.getJSONObject("home"));
    return new PlayerAvatar(color, home, current);
  }

  public static Position jsonToPosition(JSONObject jsonPosition) throws JSONException {
    int row = jsonPosition.getInt("row#");
    int col = jsonPosition.getInt("column#");
    return new Position(row, col);
  }


  private static IBoard jsonToBoard(JSONObject boardJSON, JSONObject spareJSON) throws JSONException {
    Tile spare = jsonToSpare(spareJSON);

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
//        JSONArray treasureJSON = rowGemJSON.getJSONArray(0);
//        Gem g1 = Gem.valueOf(treasureJSON.getString(0));
//        Gem g2 = Gem.valueOf(treasureJSON.getString(1));
        Gem g1 = null;
        Gem g2 = null;
        Treasure treasure = new Treasure(g1, g2);
        tiles[r][c] = new Tile(pathwayConnections, treasure);
      }
    }
    return new Board(width, height, tiles, spare);
  }

  public static Tile jsonToSpare(JSONObject spareJSON) throws JSONException {
    String connector = spareJSON.getString("connector");
    Set<Direction> pathwayConnections = symbolToDirection.get(connector);
//    Gem gem1 = Gem.valueOf(spareJSON.getString("1-image"));
//    Gem gem2 = Gem.valueOf(spareJSON.getString("2-image"));
    Gem gem1 = null;
    Gem gem2 = null;
    Treasure treasure = new Treasure(gem1, gem2);
    return new Tile(pathwayConnections, treasure);
  }








  //For XClients!
  public static List<IPlayer> jsonToPlayerSpec(JSONArray players) throws JSONException {
    List<IPlayer> result = new ArrayList<>();
//
//    for(int i=0; i<players.length(); i++){
//      JSONArray player = players.getJSONArray(i);
//      String name = player.getString(0);
//      String strategy = player.getString(1);
//      String bad = "none";
//      int count = 1;
//      boolean loop = false;
//      if (player.length() > 2) {
//        bad = player.getString(2);
//      } else{
//        IPlayer p = new TestPlayer(name, Strategy);
//      }
//
//      if (player.length() == 4) {
//        count = player.getInt(3);
//        loop = true;
//      }



    return result;
  }

  public static List<Position> jsonToGoals(JSONArray jsonPlmt) throws JSONException {
    List<Position> goals = new ArrayList<>();
    for(int i=0; i<jsonPlmt.length(); i++){
      JSONObject playerJSON = jsonPlmt.getJSONObject(i);
      Position goal = jsonToPosition(playerJSON.getJSONObject("goto"));
      goals.add(goal);
    }
    return goals;
  }


}
