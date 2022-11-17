package game.it;

import game.it.processing.IntegrationTestUtils;
import game.model.Board;
import game.model.Position;
import game.model.projections.PlayerGameProjection;

import java.util.Optional;

import player.Player;
import player.IStrategy;
import player.TurnPlan;

public class BadTestPlayer implements Player {

    protected final IStrategy strategy;
    private final String name;
    protected final BadFM badFM;

    public BadTestPlayer(String name, IStrategy strategy, BadFM badFM) {
        this.strategy = strategy;
        this.name = name;
        this.badFM = badFM;
    }


//  public TurnPlan takeTurn(PlayerGameProjection state) throws ArithmeticException {
//
//    if (this.badFM.equals(BadFM.TAKETURN)) {
//      int i = 1 / 0;
//    }
//    else {
//
//    }
//
//    return null;
//  }

    @Override
    public boolean win(boolean w) throws ArithmeticException {

        if (this.badFM.equals(BadFM.WIN)) {
            if (1 / 0 == 1);
        }

        return true;
    }

    @Override
    public boolean setup(PlayerGameProjection state, Position goal)
            throws ArithmeticException {
        if (this.badFM.equals(BadFM.SETUP)) {
            if (1 / 0 == 1);
        }

        return true;
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
    public Optional<TurnPlan> takeTurn(PlayerGameProjection game) throws ArithmeticException {

        if (this.badFM.equals(BadFM.TAKETURN)) {
            if (1 / 0 == 1);
        }

        return this.strategy.createTurnPlan(game.getBoard(), game.getSelf(),
                game.getPreviousSlideAndInsert(), IntegrationTestUtils.getCurrentGoal(game.getSelf()));
    }

    @Override
    public String getName() {
        return this.name;
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
