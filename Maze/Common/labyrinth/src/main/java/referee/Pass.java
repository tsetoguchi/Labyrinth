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

}
