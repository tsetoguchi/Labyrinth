package game.it;

import game.it.processing.IntegrationTestUtils;
import game.model.*;
import game.model.projections.PlayerGameProjection;
import game.model.projections.SelfPlayerProjection;
import player.Player;
import player.Strategy;
import player.TurnPlan;

import java.util.*;

/**
 * A simple Player implementation for testing, which proposes a completely random (but valid) board.
 */
public class TestPlayer implements Player {
    private final Strategy strategy;
    private final String name;

    public TestPlayer(String name, Strategy strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    @Override
    public Object win(boolean w) {
        return null;
    }

    @Override
    public Object setup(Optional<PlayerGameProjection> state, Position goal) {
        return 0;
    }

    @Override
    public Board proposeBoard() {
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