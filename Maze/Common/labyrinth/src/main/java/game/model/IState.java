package game.model;

import game.model.projections.StateProjection;
import java.util.List;
import java.util.Optional;

/**
 * Represents a game state with a full set of functionality that is needed to manage a State.
 * This is mainly to be given to the Referee only.
 */
public interface IState {

  GameStatus getGameStatus();

  IBoard getBoard();

  List<PlayerAvatar> getPlayerList();

  void moveActivePlayer(Position destination);

  StateProjection getStateProjection();

  void kickPlayer(PlayerAvatar player);

  void skipTurn();

  void slideAndInsert(Direction direction, int index, int rotations);

  Optional<SlideAndInsertRecord> getPreviousSlideAndInsert();


}
