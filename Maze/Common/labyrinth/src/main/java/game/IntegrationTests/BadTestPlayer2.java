package game.IntegrationTests;

import game.IntegrationTests.processing.IntegrationTestUtils;
import game.model.Position;
import game.model.projections.PlayerGameProjection;
import java.util.Optional;
import player.IStrategy;
import player.TurnPlan;

public class BadTestPlayer2 extends BadTestPlayer {

  private final int count;

  private int winCount;
  private int takeTurnCount;
  private int setupCount;

  public BadTestPlayer2(String name, IStrategy strategy, BadFM badFM, int count) {
    super(name, strategy, badFM);
    this.count = count;
    this.winCount = 0;
    this.takeTurnCount = 0;
    this.setupCount = 0;
  }

  @Override
  public boolean win(boolean w) throws ArithmeticException {
    this.winCount++;

    if (this.badFM.equals(BadFM.WIN) && this.winCount == this.count) {
      this.infiniteLoop();
    }

    return true;
  }

  /**
   * Given a view of the current game and a target tile to try to reach first, create a plan for the
   * turn.
   */
  @Override
  public Optional<TurnPlan> takeTurn(PlayerGameProjection game) throws ArithmeticException {
    this.takeTurnCount++;

    if (this.badFM.equals(BadFM.TAKETURN) && this.takeTurnCount == this.count) {
      this.infiniteLoop();
    }

    return this.strategy.createTurnPlan(game.getBoard(), game.getSelf(),
        game.getPreviousSlideAndInsert(), IntegrationTestUtils.getCurrentGoal(game.getSelf()));
  }

  @Override
  public boolean setup(Optional<PlayerGameProjection> state, Position goal)
      throws ArithmeticException {
    this.setupCount++;

    if (this.badFM.equals(BadFM.SETUP) && this.setupCount == this.count) {
      this.infiniteLoop();
    }

    return true;
  }

  private void infiniteLoop() {
    while (true) {
      // do nothing
    }
  }


}
