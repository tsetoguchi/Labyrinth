package remote.JSON;

import model.state.SlideAndInsertRecord;
import model.board.Tile;
import model.projections.StateProjection;
import model.projections.PlayerProjection;
import java.util.Optional;
import java.util.Scanner;
import org.json.JSONTokener;

public class JsonUtil {


  public static JSONTokener getInput() {
    Scanner sc = new Scanner(System.in);
    StringBuilder str = new StringBuilder();
    while (sc.hasNext()) {
      str.append(sc.next());
    }
    return new JSONTokener(str.toString());
  }

  private static JsonPlayer[] getPlmtFromGame(StateProjection game) {
    JsonPlayer[] plmt = new JsonPlayer[game.getPlayers().size()];
    for (int i = 0; i < game.getPlayers().size(); i++) {
      PlayerProjection player = game.getPlayers().get(i);
      plmt[i] = new JsonPlayer(player.getCurrentPosition(), player.getHomePosition(),
          player.getColor());
    }
    return plmt;
  }

  private static JsonTile getSpareTile(StateProjection game) {
    Tile spare = game.getBoard().getSpareTile();
    JsonTile jsonSpareTile = new JsonTile(spare.toSymbol(),
        spare.getTreasure().getGems().get(0).withDashes(),
        spare.getTreasure().getGems().get(1).withDashes());
    return jsonSpareTile;
  }

  public static JsonState getJsonState(StateProjection game) {

    JsonBoard board = new JsonBoard(game.getBoard());
    JsonTile jsonSpareTile = getSpareTile(game);

    JsonPlayer[] plmt = getPlmtFromGame(game);
    Optional<SlideAndInsertRecord> lastMove = game.getPreviousSlideAndInsert();

    JsonSlideAndInsertRecord lastAction = null;
    if (lastMove.isPresent()) {
      SlideAndInsertRecord lastMoveThing = lastMove.get();
      lastAction = new JsonSlideAndInsertRecord(lastMoveThing.getIndex(),
          lastMoveThing.getDirection());
    }
    return new JsonState(board, jsonSpareTile, plmt, lastAction);
  }


}
