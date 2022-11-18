package remote.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.model.Position;
import game.model.SlideAndInsertRecord;
import game.model.Tile;
import game.model.projections.PlayerGameProjection;
import game.model.projections.PublicPlayerProjection;

import java.util.Optional;

public class MethodJsonSerializer {

  ObjectMapper mapper;

  public MethodJsonSerializer() {
    this.mapper = new ObjectMapper();
  }

  public String generateWinJson(boolean win) {
    Boolean[] argument = new Boolean[]{
        win
    };

    Object[] output = new Object[]{
        "win",
        argument
    };
    return this.mapperExceptionHandler(output);
  }

  public String generateTakeTurnJson(PlayerGameProjection game) {

    JsonState jsonState = getJsonState(game);

    Object[] output = new Object[]{
        "take-turn",
        jsonState
    };
    return this.mapperExceptionHandler(output);
  }

  public String generateSetupJson(Optional<PlayerGameProjection> game, Position goal) {

    PlayerGameProjection gameState = null;

    if (game.isPresent()) {
      gameState = game.get();
      JsonState jsonState = getJsonState(gameState);
      Object[] output = new Object[]{
          "setup",
          jsonState,
          goal
      };
      return this.mapperExceptionHandler(output);

    } else {
      Object[] output = new Object[]{
          "setup",
          false,
          goal
      };
      return this.mapperExceptionHandler(output);
    }
  }

  private static JsonPlayer[] getPlmtFromGame(PlayerGameProjection game) {
    JsonPlayer[] plmt = new JsonPlayer[game.getPlayers().size()];
    for (int i = 0; i < game.getPlayers().size(); i++) {
      PublicPlayerProjection player = game.getPlayers().get(i);
      plmt[i] = new JsonPlayer(player.getAvatarPosition(), player.getHomePosition(),
          player.getColor());
    }
    return plmt;
  }

  private static JsonTile getSpareTile(PlayerGameProjection game) {
    Tile spare = game.getBoard().getSpareTile();
    JsonTile jsonSpareTile = new JsonTile(spare.toSymbol(),
        spare.getTreasure().getGems().get(0).withDashes(),
        spare.getTreasure().getGems().get(1).withDashes());
    return jsonSpareTile;
  }

  private static JsonState getJsonState(PlayerGameProjection game) {

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


  public String mapperExceptionHandler(Object[] output) {
    try {
      return this.mapper.writeValueAsString(output);
    } catch (Throwable throwable) {
      return throwable.getMessage();
    }
  }
}
