package game.it;

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
    public Board proposeBoard() {
        Tile[][] tileGrid = this.generateRandomTileGrid();
        Tile spareTile = this.generateRandomTile(Gem.hackmanite, Gem.hackmanite);
        return new StandardBoard(tileGrid, spareTile);
    }

    @Override
    public Optional<TurnPlan> createTurnPlan(PlayerGameProjection game) {
        return this.strategy.createTurnPlan(game.getBoard(), game.getSelf(),
                game.getPreviousSlideAndInsert(), this.getCurrentGoal(game.getSelf()));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean updateGoal(Position goal) {
        return true;
    }

    private Tile[][] generateRandomTileGrid() {
        Tile[][] tileGrid = new Tile[7][7];
        List<Gem> randomGems = Arrays.asList(Gem.values());
        randomGems.remove(Gem.hackmanite); // we will use this for the spare tile
        Collections.shuffle(randomGems);

        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                tileGrid[row][col] = this.generateRandomTile(randomGems.get(row), randomGems.get(col));
            }
        }
        return tileGrid;
    }

    private Tile generateRandomTile(Gem firstGem, Gem secondGem) {
        Random rand = new Random();
        Set<Direction> directions = new HashSet<>();
        for (Direction direction : Direction.values()) {
            if (rand.nextBoolean()) {
                directions.add(direction);
            }
        }
        return new Tile(directions, new Treasure(List.of(firstGem, secondGem)));
    }

    private Position getCurrentGoal(SelfPlayerProjection playerInformation) {
        if (playerInformation.hasReachedGoal()) {
            return playerInformation.getHomePosition();
        }
        return playerInformation.getGoalPosition();
    }
}