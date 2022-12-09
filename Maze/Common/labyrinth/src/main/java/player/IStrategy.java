package player;

import model.Position;

import model.state.StateProjection;
import referee.ITurn;

/**
 * Classes which implement this interface can create an ITurn given the information about the
 * board and the player.
 */
public interface IStrategy {

  /**
   * Given a StateProjection (the current board, spare tile, and player information), produces a
   * turn indicating which action the player will tak
   * It prioritizes getting to the given goal if possible.
   */
  ITurn createTurn(StateProjection state, Position goal);
}
