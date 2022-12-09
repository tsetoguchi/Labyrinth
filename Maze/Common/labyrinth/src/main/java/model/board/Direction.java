package model.board;

/**
 * Represents one of the four directions a slide can occur.
 */
public enum Direction {
  UP, DOWN, LEFT, RIGHT;

  /**
   * Retrieves the direction opposite of the given direction
   */
  public static Direction opposite(Direction direction) {
    if (direction == UP) {
      return DOWN;
    }
    if (direction == DOWN) {
      return UP;
    }
    if (direction == LEFT) {
      return RIGHT;
    }
    if (direction == RIGHT) {
      return LEFT;
    }
    throw new IllegalArgumentException("Direction was not an expected direction");
  }
}
