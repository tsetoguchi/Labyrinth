package model.board;

import model.Position;
import referee.Turn;

import static model.board.Direction.DOWN;
import static model.board.Direction.LEFT;
import static model.board.Direction.RIGHT;
import static model.board.Direction.UP;

public class DefaultRules implements IRules {



  public DefaultRules() {
  }

  public boolean isValidSlideAndInsert(Turn turn, int width, int height) {
    int index = turn.getSlideIndex();
    Direction direction = turn.getSlideDirection();
    int rotations = turn.getSpareTileRotations();

    boolean isEvenIndex = index % 2 == 0;
    boolean isPositiveIndex = index >= 0;
    boolean indexWithinBounds = ((direction == UP || direction == DOWN) && index < height)
        || ((direction == LEFT || direction == RIGHT) && index < width);
    boolean isPositiveRotations = rotations >= 0;

    return isEvenIndex && isPositiveIndex && indexWithinBounds && isPositiveRotations;
  }

  @Override
  public boolean immovablePosition(Position position) {
    return position.getRow() % 2 != 0 && position.getColumn() % 2 != 0;
  }

}
