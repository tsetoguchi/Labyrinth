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
import model.Utils;
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
 * Represents a JSON deserializer for Labyrinth.
 */
public class JsonDeserializer {


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
  }

  /**
   * Deserializes a JSON state into an IState.
   */
  public static IState state(JSONObject game) throws JSONException {
    IBoard board = board(game.getJSONObject("board"), game.getJSONObject("spare"));
    List<PlayerAvatar> playerAvatars = playerAvatars(game.getJSONArray("plmt"));

    if (!game.isNull("last")) {
      SlideAndInsertRecord previousTurn = lastTurn(game.getJSONArray("last"));
      return new State(board, playerAvatars, previousTurn);
    }
    return new State(board, playerAvatars);
  }

  /**
   * Deserializes a JSON last into a SlideAndInsertRecord.
   */
  private static SlideAndInsertRecord lastTurn(JSONArray last) throws JSONException {
    int index = last.getInt(0);
    Direction direction = Direction.valueOf(last.getString(1));
    return new SlideAndInsertRecord(direction, index, 0);
  }

  /**
   * Deserializes a JSON plmt into a List<PlayerAvatar>.
   */
  private static List<PlayerAvatar> playerAvatars(JSONArray plmt) throws JSONException {
    List<PlayerAvatar> players = new ArrayList<>();
    for (int i = 0; i < plmt.length(); i++) {
      players.add(playerAvatar(plmt.getJSONObject(i)));
    }
    return players;
  }

  /**
   * Deserializes a JSON Player into a PlayerAvatar.
   */
  private static PlayerAvatar playerAvatar(JSONObject jsonPlayer) throws JSONException {
    Color color = Utils.stringToColor(jsonPlayer.getString("color"));
    Position current = coordinate(jsonPlayer.getJSONObject("current"));
    Position home = coordinate(jsonPlayer.getJSONObject("home"));
    return new PlayerAvatar(color, home, current);
  }

  /**
   * Deserializes a JSON Coordinate into a Position.
   */
  public static Position coordinate(JSONObject jsonCoordinate) throws JSONException {
    int row = jsonCoordinate.getInt("row#");
    int col = jsonCoordinate.getInt("column#");
    return new Position(row, col);
  }


  /**
   * Deserializes a JSON Board and Spare Tile into an IBoard.
   */
  public static IBoard board(JSONObject boardJSON, JSONObject spareJSON) throws JSONException {
    Tile spare = spare(spareJSON);

    JSONArray rowsJSON = boardJSON.getJSONArray("connectors");
    JSONArray rowsGemJSON = boardJSON.getJSONArray("treasures");

    int height = rowsJSON.length();
    int width = rowsJSON.getJSONArray(0).length();

    Tile[][] tiles = new Tile[height][width];

    for (int r = 0; r < height; r++) {
      JSONArray rowJSON = rowsJSON.getJSONArray(r);
      JSONArray rowGemJSON = rowsGemJSON.getJSONArray(r);
      for (int c = 0; c < width; c++) {
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

  /**
   * Deserializes a JSON Spare Tile into a Tile.
   */
  public static Tile spare(JSONObject spareJSON) throws JSONException {
    String connector = spareJSON.getString("tilekey");
    Set<Direction> pathwayConnections = symbolToDirection.get(connector);
    Gem gem1 = Gem.getGem(spareJSON.getString("1-image"));
    Gem gem2 = Gem.getGem(spareJSON.getString("2-image"));
    Treasure treasure = new Treasure(gem1, gem2);
    return new Tile(pathwayConnections, treasure);
  }

  /**
   * Deserializes a JSON RefereeState or RefereeState2 into a List<Positions> (list of goals).
   */
  public static List<Position> goals(JSONObject jsonGame) throws JSONException {
    JSONArray jsonPlmt = jsonGame.getJSONArray("plmt");
    List<Position> goals = new ArrayList<>();
    for (int i = 0; i < jsonPlmt.length(); i++) {
      JSONObject playerJSON = jsonPlmt.getJSONObject(i);
      Position goal = coordinate(playerJSON.getJSONObject("goto"));
      goals.add(goal);
    }

    if (!jsonGame.isNull("goals")) {
      JSONArray jsonGoals = jsonGame.getJSONArray("goals");
      for (int i = 0; i < jsonGoals.length(); i++) {
        JSONObject jsonCoordinate = jsonGoals.getJSONObject(i);
        Position p = JsonDeserializer.coordinate(jsonCoordinate);
        goals.add(p);
      }
    }

    return goals;
  }

  /**
   * Deserializes a JSON Move (choice of four values) into a move.
   */
  public static Move move(JSONArray moveJSON) throws JSONException {
    int index = moveJSON.getInt(0);
    Direction direction = Direction.valueOf(moveJSON.getString(1));
    int rotations = Utils.degreesToRotations(moveJSON.getInt(2));
    Position moveTo = JsonDeserializer.coordinate(moveJSON.getJSONObject(3));
    return new Move(direction, index, rotations, moveTo);
  }




}
