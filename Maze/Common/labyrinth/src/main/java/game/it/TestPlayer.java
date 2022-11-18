package game.it;

import game.it.processing.IntegrationTestUtils;
import game.model.*;
import game.model.projections.PlayerGameProjection;
import player.IPlayer;
import player.IStrategy;
import player.TurnPlan;

import java.util.*;

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

    @Override
    public Board proposeBoard(int rows, int columns) {
        Tile[][] tileGrid = IntegrationTestUtils.generateRandomTileGrid();
        Tile spareTile = IntegrationTestUtils.generateRandomTile(Gem.hackmanite, Gem.hackmanite);
        return new StandardBoard(tileGrid, spareTile);
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