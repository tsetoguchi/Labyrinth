package game.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import protocol.serialization.model.TileSerializer;

import java.util.*;

import static game.model.Direction.*;

/**
 * Represents a single tile on a Labyrinth board.
 */
@JsonSerialize(using = TileSerializer.class)
public class Tile {
    private Set<Direction> pathwayConnections;

    private final Treasure treasure;

    /**
     * Creates a new Tile with the given treasure and pathways leading in the given Directions.
     */
    public Tile(Set<Direction> pathwayConnections, Treasure treasure) {
        this.validateMinimumPathways(pathwayConnections);
        this.pathwayConnections = new HashSet<>();
        this.pathwayConnections.addAll(pathwayConnections);
        this.treasure = treasure;
    }

    /**
     * Convenience constructor used for testing purposes.
     */
    Tile(boolean up, boolean down, boolean left, boolean right, Treasure treasure) {
        this.pathwayConnections = new HashSet<>();
        if (up) {
            this.pathwayConnections.add(UP);
        }
        if (down) {
            this.pathwayConnections.add(DOWN);
        }
        if (left) {
            this.pathwayConnections.add(LEFT);
        }
        if (right) {
            this.pathwayConnections.add(RIGHT);
        }
        this.validateMinimumPathways(this.pathwayConnections);
        this.treasure = treasure;
    }

    /**
     * @return whether the pathway on this Tile connects to edge of the tile in the given direction
     * from the center of the Tile.
     */
    public boolean connects(Direction direction) {
        return this.pathwayConnections.contains(direction);
    }

    /**
     * Rotates the Tile 90 degrees clockwise the given number of rotations.
     * @param rotations The number of clockwise rotations.
     */
    public void rotate(int rotations) {
        if (rotations < 0) {
            throw new IllegalArgumentException("Tried to rotate a tile a negative number of times.");
        }
        for (int i = 0; i < rotations; i++) {
            this.rotateOnce();
        }
    }

    public Treasure getTreasure() {
        return this.treasure;
    }

    /**
     * Checks equality of Tiles based on the equality of their Treasures, relying on the fact that
     * no two Tiles share a Treasure.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Tile otherTile = (Tile) o;
        return this.treasure.equals(otherTile.treasure);
    }

    /**
     * Hashes a Tile based on its Treasure, relying on the fact that no two Tiles share a Treasure.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.treasure);
    }

    @Override
    public String toString() {
        return "Tile{" +
                "pathwayConnections=" + this.pathwayConnections +
                ", treasure=" + this.treasure +
                '}';
    }

    private void validateMinimumPathways(Set<Direction> directions) {
        if (directions.size() < 2) {
            throw new IllegalArgumentException("Attempted to create a Tile with fewer than two pathways (invalid).");
        }
    }

    /**
     * Rotates the Tile 90 degrees clockwise.
     */
    private void rotateOnce() {
        Set<Direction> nextPathwayConnections = new HashSet<>();

        if (this.pathwayConnections.contains(LEFT)) {
            nextPathwayConnections.add(UP);
        }
        if (this.pathwayConnections.contains(UP)) {
            nextPathwayConnections.add(RIGHT);
        }
        if (this.pathwayConnections.contains(RIGHT)) {
            nextPathwayConnections.add(DOWN);
        }
        if (this.pathwayConnections.contains(DOWN)) {
            nextPathwayConnections.add(LEFT);
        }
        this.pathwayConnections = nextPathwayConnections;
    }

    private static final Map<Set<Direction>, String> directionToSymbol = new HashMap<>();

    static {
        directionToSymbol.put(Set.of(UP, DOWN), "│");
        directionToSymbol.put(Set.of(RIGHT, LEFT), "─");
        directionToSymbol.put(Set.of(DOWN, LEFT), "┐");
        directionToSymbol.put(Set.of(UP, RIGHT), "└");
        directionToSymbol.put(Set.of(RIGHT, DOWN), "┌");
        directionToSymbol.put(Set.of(UP, LEFT), "┘");
        directionToSymbol.put(Set.of(RIGHT, DOWN, LEFT), "┬");
        directionToSymbol.put(Set.of(UP, RIGHT, DOWN), "├");
        directionToSymbol.put(Set.of(UP, RIGHT, LEFT), "┴");
        directionToSymbol.put(Set.of(UP, DOWN, LEFT), "┤");
        directionToSymbol.put(Set.of(UP, RIGHT, DOWN, LEFT), "┼");
    }

    public String toSymbol() {
        return Tile.directionToSymbol.get(this.pathwayConnections);
    }

    public Tile deepCopy() {
        Set<Direction> newDirections = new HashSet<>();
        if (this.pathwayConnections.contains(Direction.UP)) {newDirections.add(Direction.UP);}
        if (this.pathwayConnections.contains(Direction.DOWN)) {newDirections.add(Direction.DOWN);}
        if (this.pathwayConnections.contains(Direction.LEFT)) {newDirections.add(Direction.LEFT);}
        if (this.pathwayConnections.contains(Direction.RIGHT)) {newDirections.add(Direction.RIGHT);}

        return new Tile(newDirections, this.treasure);
    }
}
