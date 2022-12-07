package player;

import model.Position;
import model.state.StateProjection;
import referee.ITurn;
import referee.Pass;

/**
 * Represents a strategy that only passes.
 */
public class PassStrategy implements IStrategy {

  @Override
  public ITurn createTurn(StateProjection state, Position goal) {
    return new Pass();
  }
}
