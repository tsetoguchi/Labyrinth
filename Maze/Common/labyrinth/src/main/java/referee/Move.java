package referee;

import model.board.Direction;
import model.Position;

/**
 * The set of actions that a player intends to perform on its turn.
 */
public class Move implements ITurn{

  private final Direction slideDirection;
  private final int slideIndex;
  private final int spareTileRotations;
  private final Position moveDestination;

  public Move(Direction slideDirection, int slideIndex, int spareTileRotations,
              Position moveDestination) {
    this.slideDirection = slideDirection;
    this.slideIndex = slideIndex;
    this.spareTileRotations = spareTileRotations;
    this.moveDestination = moveDestination;
  }

  public Direction getSlideDirection() {
    return this.slideDirection;
  }

  public int getSlideIndex() {
    return this.slideIndex;
  }

  public int getSpareTileRotations() {
    return this.spareTileRotations;
  }

  public Position getMoveDestination() {
    return this.moveDestination;
  }

  @Override
  public boolean isMove() {
    return true;
  }

  @Override
  public Move getMove() {
    return null;
  }
}