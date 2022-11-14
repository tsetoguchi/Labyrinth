package game.model;

import static game.model.Direction.DOWN;
import static game.model.Direction.LEFT;
import static game.model.Direction.RIGHT;
import static game.model.Direction.UP;

import java.util.List;

public class DefaultRules implements IRules {

  private static final int width = 7;
  private static final int height = 7;

  /**
   * Validate that sliding a row or column at the given index in the specified direction does not violate game
   * rules or pass invalid arguments.
   */
  public boolean isValidSlideAndInsert(Direction direction, int index, int rotations) {
    boolean isEvenIndex = index % 2 == 0;
    boolean isPositiveIndex = index >= 0;
    boolean indexWithinBounds = ((direction == UP || direction == DOWN) && index < height)
        || ((direction == LEFT || direction == RIGHT) && index < width);
    boolean isPositiveRotations = rotations >= 0;

    return isEvenIndex && isPositiveIndex && indexWithinBounds && isPositiveRotations;
  }


}
