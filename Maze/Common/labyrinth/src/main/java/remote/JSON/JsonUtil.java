package remote.JSON;

import game.model.SlideAndInsertRecord;
import game.model.Tile;
import game.model.projections.PlayerStateProjection;
import game.model.projections.PublicPlayerProjection;
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

  private static JsonPlayer[] getPlmtFromGame(PlayerStateProjection game) {
    JsonPlayer[] plmt = new JsonPlayer[game.getPlayers().size()];
    for (int i = 0; i < game.getPlayers().size(); i++) {
      PublicPlayerProjection player = game.getPlayers().get(i);
      plmt[i] = new JsonPlayer(player.getAvatarPosition(), player.getHomePosition(),
          player.getColor());
    }
    return plmt;
  }

  private static JsonTile getSpareTile(PlayerStateProjection game) {
    Tile spare = game.getBoard().getSpareTile();
    JsonTile jsonSpareTile = new JsonTile(spare.toSymbol(),
        spare.getTreasure().getGems().get(0).withDashes(),
        spare.getTreasure().getGems().get(1).withDashes());
    return jsonSpareTile;
  }

  public static JsonState getJsonState(PlayerStateProjection game) {

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
