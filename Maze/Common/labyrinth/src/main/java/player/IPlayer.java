package player;

import model.board.IBoard;
import model.Position;
import model.state.StateProjection;
import referee.ITurn;

import java.util.Optional;

/**
 * The interface of a player of the game Labyrinth. This player receives/sends all the necessary information
 * to carry out a game.
 */
public interface IPlayer {

  /**
   * Given a projection of the current gamestate, the player returns their turn.
   * The player assumes it is the player currently up (active player) in the gamestate projection.
   */
  ITurn takeTurn(StateProjection game);

  /**
   * At the first call of the game, the player receives a StateProjection in an optional to
   * indicate the starting state of the game. In subsequent calls, this will be empty.
   * The player also receives their next destination. This is either a goal position or the position
   * of the player's home if there are no more goals.
   * Returns true to acknowledge it has received the call.
   */
  boolean setup(Optional<StateProjection> game, Position goal);

  /**
   * Tells the player if they won or not.
   * Returns true to acknowledge it has received the call.
   */
  boolean win(boolean playerWon);

  /**
   * The player is asked to propose a board for the game given the dimensions.
   */
  IBoard proposeBoard(int rows, int columns);

  /**
   * Returns the name of the player
   */
  String getName();

}
