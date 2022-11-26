package game.IntegrationTests;

import game.IntegrationTests.processing.IntegrationTestUtils;
import game.model.*;
import game.model.projections.PlayerGameProjection;
import player.IStrategy;
import player.TurnPlan;

import java.util.*;
import referee.PlayerResult;
import player.IPlayer;

/**
 * A simple Player implementation for testing, which proposes a completely random (but valid) board.
 */
public class TestPlayer implements IPlayer {
    private final IStrategy strategy;
    private final String name;

    public TestPlayer(String name, IStrategy strategy) {
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


    public Board proposeBoard(int rows, int columns) {
        Tile[][] tileGrid = IntegrationTestUtils.generateRandomTileGrid();
        Tile spareTile = IntegrationTestUtils.generateRandomTile(Gem.hackmanite, Gem.hackmanite);
        return new StandardBoard(tileGrid, spareTile);
    }


    public void returnHome(Position homeTile) {

    }

    @Override
    public void informGameEnd(GameStatus status, PlayerResult result) {

    }


    public String getPlayerName() {
        return null;
    }


    public Optional<TurnPlan> takeTurn(PlayerGameProjection game) {
        return this.strategy.createTurnPlan(game.getBoard(), game.getSelf(),
                game.getPreviousSlideAndInsert(), IntegrationTestUtils.getCurrentGoal(game.getSelf()));
    }


    public String getName() {
        return this.name;
    }


    public boolean updateGoal(Position goal) {
        return true;
    }




}