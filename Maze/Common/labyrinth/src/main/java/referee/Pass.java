package referee;

import java.util.NoSuchElementException;

/**
 * A pass represents a turn of the Labyrinth game in which the player does not act.
 */
public class Pass implements ITurn {

  @Override
  public boolean isMove() {
    return false;
  }

  @Override
  public Move getMove() {
    throw new NoSuchElementException();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Pass)) {
      return false;
    }
    return true;
  }

}
