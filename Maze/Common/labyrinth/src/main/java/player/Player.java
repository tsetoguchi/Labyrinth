package player;

import game.IntegrationTests.processing.IntegrationTestUtils;
import game.model.*;
import game.model.projections.PlayerGameProjection;

import java.util.Optional;
import referee.PlayerResult;

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
    public boolean setup(Optional<PlayerGameProjection> state, Position goal) {
        return true;
    }

    @Override
    public Board proposeBoard(int rows, int columns) {
        return new FlexibleBoard(columns, rows);
    }

    @Override
    public void returnHome(Position homeTile) {

    }

    @Override
    public void informGameEnd(GameStatus status, PlayerResult result) {

    }

    @Override
    public String getPlayerName() {
        return null;
    }

    @Override
    public Optional<TurnPlan> takeTurn(PlayerGameProjection game) {
        return this.strategy.createTurnPlan(game.getBoard(), game.getSelf(),
                game.getPreviousSlideAndInsert(), IntegrationTestUtils.getCurrentGoal(game.getSelf()));
    }

    @Override
    public boolean updateGoal(Position goal) {
        return true;
    }


}
