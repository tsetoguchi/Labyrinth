package referee.clients;

import game.model.Board;
import game.model.GameStatus;
import game.model.Position;
import game.model.projections.PlayerGameProjection;
import player.TurnPlan;
import referee.PlayerResult;

import java.util.Optional;

//TODO: documentation and exceptions for bad players

/**
 * A remote proxy for the player. The playerName should be stored locally as a variable.
 */
public interface IPlayerInterface {


  Optional<TurnPlan> takeTurn(PlayerGameProjection game);

  boolean setup(Optional<PlayerGameProjection> game, Position goal);

  boolean win(boolean playerWon);

  Board proposeBoard(int rows, int columns);

  void returnHome(Position homeTile);

  void informGameEnd(GameStatus status, PlayerResult result);

  String getPlayerName();

  /**
   * Accepts the current game state and the current goal and updates accordingly. returning True if
   * the player successfully responds to setup.
   */
  boolean updateGoal(Position goal);
}
