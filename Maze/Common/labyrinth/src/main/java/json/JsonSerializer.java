package json;

import java.util.List;
import java.util.Optional;
import model.Position;
import model.Utils;
import model.board.IBoard;
import model.board.Tile;
import model.state.PlayerAvatar;
import model.state.SlideAndInsertRecord;
import model.state.StateProjection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import referee.GameResults;
import referee.Move;

/**
 * Represents a JSON serializer for Labyrinth.
 */
public class JsonSerializer {

  /**
   * Serializes a Move into a JSON Move.
   */
  public static JSONArray move(Move move) throws JSONException {
    JSONArray resultArray = new JSONArray();
    resultArray.put(move.getSlideIndex());
    resultArray.put(move.getSlideDirection());
    resultArray.put(rotationsToDegrees(move.getSpareTileRotations()));
    resultArray.put(JsonSerializer.coordinate(move.getMoveDestination()));
    return resultArray;
  }


  /**
   * Serializes a StateProjection into a JSON State.
   */
  public static JSONObject stateProjection(StateProjection game) throws JSONException {
    JSONObject result = new JSONObject();
    JSONObject board = board(game.getBoard());
    JSONObject spare = tile(game.getBoard().getSpareTile());
    JSONArray plmt = plmt(game.getPlayers());
    JSONArray last = last(game.getPreviousSlideAndInsert());

    result.put("board", board);
    result.put("spare", spare);
    result.put("plmt", plmt);
    result.put("last", last);

    return result;
  }

  /**
   * Serializes an IBoard into a JSON Board.
   */
  public static JSONObject board(IBoard board) throws JSONException {
    JSONObject result = new JSONObject();
    JSONArray connectors = new JSONArray();
    JSONArray treasures = new JSONArray();

    for (int row = 0; row < board.getHeight(); row++) {

      JSONArray currentConnectorsRow = new JSONArray();
      JSONArray currentTreasureRow = new JSONArray();

      for (int col = 0; col < board.getWidth(); col++) {

        Position currentPosition = new Position(row, col);
        Tile currentTile = board.getTileAt(currentPosition);

        String currentConnectors = currentTile.toSymbol();
        String[] currentTreasure = new String[2];//currentTile.getTreasure().toStringArray();
        currentTreasure[0] = "aplite";
        currentTreasure[1] = "aplite";

        currentConnectorsRow.put(currentConnectors);
        currentTreasureRow.put(currentTreasure);
      }
      connectors.put(currentConnectorsRow);
      treasures.put(currentTreasureRow);
    }

    result.put("connectors", connectors);
    result.put("treasures", treasures);

    return result;
  }

  /**
   * Serializes a Tile into a JSON Tile.
   */
  public static JSONObject tile(Tile tile) throws JSONException {
    JSONObject result = new JSONObject();
    result.put("tilekey", tile.toSymbol());
    result.put("1-image", "aplite");//tile.getTreasure().getGems().get(0).toString());
    result.put("2-image", "aplite");//tile.getTreasure().getGems().get(1).toString());
    return result;
  }

  public static int rotationsToDegrees(int spareTileRotations) {
    int netClockwiseRotations = spareTileRotations % 4;
    int netCounterclockwiseRotations = (4 - netClockwiseRotations) % 4;
    return 90 * netCounterclockwiseRotations;
  }

  /**
   * Serializes a GameResults into a JSON array [[winner names],[eliminated names]].
   */
  public static JSONArray gameResults(GameResults results) {
    JSONArray winnersJSON = new JSONArray(results.getWinners());
    JSONArray eliminatedJSON = new JSONArray(results.getEliminated());
    JSONArray result = new JSONArray();
    result.put(winnersJSON);
    result.put(eliminatedJSON);
    return result;
  }

  /**
   * Serializes a Position into a JSON Coordinate.
   */
  public static JSONObject coordinate(Position position) throws JSONException {
    JSONObject result = new JSONObject();
    result.put("row#", position.getRow());
    result.put("column#", position.getColumn());
    return result;
  }

  /**
   * Serializes a PlayerAvatar into a JSON Player.
   */
  public static JSONObject player(PlayerAvatar player) throws JSONException {
    JSONObject result = new JSONObject();
    result.put("current", coordinate(player.getCurrentPosition()));
    result.put("home", coordinate(player.getHome()));
    result.put("color", Utils.colorToString(player.getColor()));
    return result;
  }

  /**
   * Serializes a List<PlayerAvatar> into a JSON Plmt.
   */
  public static JSONArray plmt(List<PlayerAvatar> players) throws JSONException {
    JSONArray plmt = new JSONArray();
    for (PlayerAvatar player : players) {
      plmt.put(player(player));
    }
    return plmt;
  }

  /**
   * Serializes a Optional<SlideAndInsertRecord> into null if empty, or a JSON Action if present.
   */
  public static JSONArray last(Optional<SlideAndInsertRecord> last) {

    if (last.isEmpty()) {
      return null;
    }

    JSONArray action = new JSONArray();
    action.put(last.get().getIndex());
    action.put(last.get().getDirection());
    return action;
  }

  // Serialization into Method Call Formats
  // [ MName, [Argument, ...] ]

  /**
   * Serializes a StateProjection into a JSON take-turn in method call format.
   */
  public static JSONArray takeTurn(StateProjection game) throws JSONException {
    JSONArray result = new JSONArray();
    result.put("take-turn");
    JSONArray args = new JSONArray();
    args.put(JsonSerializer.stateProjection(game));
    result.put(args);
    return result;
  }

  /**
   * Serializes a boolean into a JSON win in method call format.
   */
  public static JSONArray win(boolean won) {
    JSONArray result = new JSONArray();
    result.put("win");
    JSONArray args = new JSONArray();
    args.put(won);
    result.put(args);
    return result;
  }

  /**
   * Serializes an Optional<StateProjection> and a Position into a JSON setup in method call
   * format.
   */
  public static JSONArray setup(Optional<StateProjection> game, Position goal)
      throws JSONException {
    JSONArray result = new JSONArray();
    result.put("setup");
    JSONArray args = new JSONArray();
    if (game.isPresent()) {
      args.put(JsonSerializer.stateProjection(game.get()));
    } else {
      args.put(false);
    }
    args.put(JsonSerializer.coordinate(goal));

    result.put(args);
    return result;
  }

}
