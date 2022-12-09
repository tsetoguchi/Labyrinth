package referee;

import model.Position;
import model.board.Direction;

import static model.board.Direction.DOWN;
import static model.board.Direction.LEFT;
import static model.board.Direction.RIGHT;
import static model.board.Direction.UP;

/**
 * A set of default rules to be played in the game.
 */
public class DefaultRules implements IRules {


  public DefaultRules() {
  }

  /**
   * The basic rules of the game. SLide only even indices and positive rotations.
   */
  public boolean isValidSlideAndInsert(Move move, int width, int height) {
    int index = move.getSlideIndex();
    Direction direction = move.getSlideDirection();
    int rotations = move.getSpareTileRotations();

    boolean isEvenIndex = index % 2 == 0;
    boolean isPositiveIndex = index >= 0;
    boolean indexWithinBounds = ((direction == UP || direction == DOWN) && index < height)
        || ((direction == LEFT || direction == RIGHT) && index < width);
    boolean isPositiveRotations = rotations >= 0;

    return isEvenIndex && isPositiveIndex && indexWithinBounds && isPositiveRotations;
  }

  /**
   * Odd indices are not slideable. Returns true if the row and column is odd.
   */
  @Override
  public boolean immovablePosition(Position position) {
    return position.getRow() % 2 != 0 && position.getColumn() % 2 != 0;
  }

}
