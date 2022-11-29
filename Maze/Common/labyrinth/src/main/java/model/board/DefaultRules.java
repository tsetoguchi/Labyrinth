package model.board;

import model.board.Direction;
import model.board.IRules;

import static model.board.Direction.DOWN;
import static model.board.Direction.LEFT;
import static model.board.Direction.RIGHT;
import static model.board.Direction.UP;

public class DefaultRules implements IRules {

  private final int width;
  private final int height;

  public DefaultRules(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public boolean isValidSlideAndInsert(Direction direction, int index, int rotations) {
    boolean isEvenIndex = index % 2 == 0;
    boolean isPositiveIndex = index >= 0;
    boolean indexWithinBounds = ((direction == UP || direction == DOWN) && index < this.height)
        || ((direction == LEFT || direction == RIGHT) && index < this.width);
    boolean isPositiveRotations = rotations >= 0;

    return isEvenIndex && isPositiveIndex && indexWithinBounds && isPositiveRotations;
  }

}
