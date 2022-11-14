package game.model;

import java.util.List;
import java.util.Optional;

/**
 * Represents a Game state with a full set of functionality that is needed to manage a Game.
 * This is mainly to be given to the Referee only.
 */
public interface PrivateGameState {

  GameStatus getGameStatus();

  PlayerAvatar getActivePlayer();

  Board getBoard();

  List<PlayerAvatar> getPlayerList();

  void moveActivePlayer(Position destination);

  void kickActivePlayer();

  void skipTurn();

  void slideAndInsert(Direction direction, int index, int rotations);

  Optional<SlideAndInsertRecord> getPreviousSlideAndInsert();


}
