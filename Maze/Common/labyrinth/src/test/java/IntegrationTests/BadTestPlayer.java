package IntegrationTests;

import game.Utils;
import game.model.IBoard;
import game.model.GameStatus;
import game.model.Position;
import game.model.projections.PlayerStateProjection;

import java.util.Optional;

import player.IStrategy;
import player.Turn;
import referee.PlayerResult;
import player.IPlayer;

public class BadTestPlayer implements IPlayer {

    protected final IStrategy strategy;
    private final String name;
    protected final BadFM badFM;

    private final Position goal;
    public BadTestPlayer(String name, IStrategy strategy, BadFM badFM) {
        this.strategy = strategy;
        this.name = name;
        this.badFM = badFM;
    }


    @Override
    public boolean win(boolean w) throws ArithmeticException {

        if (this.badFM.equals(BadFM.WIN)) {
            if (1 / 0 == 1);
        }

        return true;
    }

    @Override
    public boolean setup(Optional<PlayerStateProjection> state, Position goal)
            throws ArithmeticException {
        if (this.badFM.equals(BadFM.SETUP)) {
            if (1 / 0 == 1);
        }

        return true;
    }

    /**
     * Propose a board layout for the game.
     **/

    public IBoard proposeBoard(int rows, int columns) {
        return null;
    }


    public void returnHome(Position homeTile) {

    }


    public void informGameEnd(GameStatus status, PlayerResult result) {

    }


    public String getPlayerName() {
        return null;
    }

    /**
     * Given a view of the current game and a target tile to try to reach first, create a plan for the
     * turn.
     *
     * @param game
     */
    @Override
    public Optional<Turn> takeTurn(PlayerStateProjection game) throws ArithmeticException {

        if (this.badFM.equals(BadFM.TAKETURN)) {
            if (1 / 0 == 1);
        }

        return this.strategy.createTurnPlan(game.getBoard(), game.getSelf(),
                game.getPreviousSlideAndInsert(), this.goal);
    }


    public String getName() {
        return this.name;
    }

    /**
     * Accept a new goal from the referee. Returns true if update was successful and false otherwise.
     *
     * @param goal
     */

    public boolean updateGoal(Position goal) {
        return false;
    }
}
