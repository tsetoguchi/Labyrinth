package game.it;

import game.model.Board;
import game.model.Game;
import game.model.Position;
import game.model.projections.PlayerGameProjection;
import java.util.Optional;
import player.Player;
import player.Strategy;
import player.TurnPlan;

public class BadTestPlayer implements Player {

  private final Strategy strategy;
  private final String name;
  private final BadFM badFM;

  public BadTestPlayer(Strategy strategy, String name, BadFM badFM) {
    this.strategy = strategy;
    this.name = name;
    this.badFM = badFM;
  }


  @Override
  public TurnPlan takeTurn(Game state) throws ArithmeticException {

    if (this.badFM.equals(BadFM.TAKETURN)) {
      int i = 1 / 0;
    }

    return null;
  }

  @Override
  public Object win(boolean w) throws ArithmeticException {

    if (this.badFM.equals(BadFM.WIN)) {
      int i = 1 / 0;
    }

    return null;
  }

  @Override
  public Object setup(Optional<PlayerGameProjection> state, Position goal)
      throws ArithmeticException {

    if (this.badFM.equals(BadFM.SETUP)) {
      int i = 1 / 0;
    }

    return null;
  }

  /**
   * Propose a board layout for the game.
   **/
  @Override
  public Board proposeBoard() {
    return null;
  }

  /**
   * Given a view of the current game and a target tile to try to reach first, create a plan for the
   * turn.
   *
   * @param game
   */
  @Override
  public Optional<TurnPlan> createTurnPlan(PlayerGameProjection game) {
    return Optional.empty();
  }

  @Override
  public String getName() {
    return null;
  }

  /**
   * Accept a new goal from the referee. Returns true if update was successful and false otherwise.
   *
   * @param goal
   */
  @Override
  public boolean updateGoal(Position goal) {
    return false;
  }
}
