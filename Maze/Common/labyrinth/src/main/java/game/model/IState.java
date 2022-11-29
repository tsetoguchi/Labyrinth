package game.model;

import java.util.List;
import java.util.Optional;

/**
 * Represents a game state with a full set of functionality that is needed to manage a State.
 * This is mainly to be given to the Referee only.
 */
public interface IState {

  GameStatus getGameStatus();

  PlayerAvatar getActivePlayer();

  IBoard getBoard();

  List<PlayerAvatar> getPlayerList();

  void moveActivePlayer(Position destination);

  void kickActivePlayer();

  void kickPlayer(PlayerAvatar player);

  void skipTurn();

  void slideAndInsert(Direction direction, int index, int rotations);

  Optional<SlideAndInsertRecord> getPreviousSlideAndInsert();


}
