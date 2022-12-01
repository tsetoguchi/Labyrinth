package player;

import model.Position;

import model.projections.StateProjection;
import referee.ITurn;

/**
 * Classes which implement this interface can create a Move given the information about the
 * board and the player.
 */
public interface IStrategy {

  /**
   * Given the current board, spare tile, and player information, produces a plan for the turn which
   * includes all the actions the player wishes to take, prioritizing getting to the given goal if
   * possible.
   * @return
   */
  ITurn createTurn(StateProjection state, Position goal);
}
