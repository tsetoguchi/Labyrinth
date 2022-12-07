package referee;

import java.util.NoSuchElementException;

public class Pass implements ITurn{

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
