package player;

import game.model.*;
import game.model.projections.PlayerStateProjection;

import java.util.Optional;

/**
 * Contains the logic involved in a player's decision making, handling a state and returns TurnPlans.
 */
public class Player implements IPlayer {

    protected String name;
    protected IStrategy strategy;
    protected Position destination;
    protected Optional<Boolean> winner;

    public Player(String name, IStrategy strategy) {
        this.name = name;
        this.strategy = strategy;
        this.winner = Optional.empty();
    }

    @Override
    public boolean win(boolean w) {
        this.winner = Optional.of(w);
        return true;
    }

    @Override
    public boolean setup(Optional<PlayerStateProjection> state, Position goal) {
        this.destination = goal;
        return true;
    }

    @Override
    public Optional<Turn> takeTurn(PlayerStateProjection game) {
        return this.strategy.createTurnPlan(game.getBoard(), game.getSelf(),
                game.getPreviousSlideAndInsert(), this.destination);
    }

    @Override
    public String getPlayerName() {
        return this.name;
    }

    @Override
    public IBoard proposeBoard(int rows, int columns) {
        return new FlexibleBoard(columns, rows);
    }

}
