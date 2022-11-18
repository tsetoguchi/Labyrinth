package player;

import game.it.processing.IntegrationTestUtils;
import game.model.*;
import game.model.projections.PlayerGameProjection;

import java.util.Optional;

/**
 * Contains the logic involved in a player's decision making, handling a state and returns TurnPlans.
 */
public class Player implements IPlayer {

    private String name;
    private IStrategy strategy;



    public Player(String name, IStrategy strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    @Override
    public boolean win(boolean w) {
        return true;
    }

    @Override
    public boolean setup(PlayerGameProjection state, Position goal) {
        return true;
    }

    @Override
    public Board proposeBoard(int rows, int columns) {
        return new FlexibleBoard(columns, rows);
    }

    @Override
    public Optional<TurnPlan> takeTurn(PlayerGameProjection game) {
        return this.strategy.createTurnPlan(game.getBoard(), game.getSelf(),
                game.getPreviousSlideAndInsert(), IntegrationTestUtils.getCurrentGoal(game.getSelf()));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean updateGoal(Position goal) {


        return true;
    }


}
