package game.it.processing;

import game.model.*;

import java.util.*;

import static game.model.Direction.*;

public class IntegrationTestUtils {
    static Map<String, Set<Direction>> symbolToDirection = new HashMap<>();
    static Map<Set<Direction>, String> directionToSymbol = new HashMap<>();
    static {
        symbolToDirection.put("│", Set.of(UP, DOWN));
        symbolToDirection.put("─", Set.of(RIGHT, LEFT));
        symbolToDirection.put("┐", Set.of(DOWN, LEFT));
        symbolToDirection.put("└", Set.of(UP, RIGHT));
        symbolToDirection.put("┌", Set.of(RIGHT, DOWN));
        symbolToDirection.put("┘", Set.of(UP, LEFT));
        symbolToDirection.put("┬", Set.of(RIGHT, DOWN, LEFT));
        symbolToDirection.put("├", Set.of(UP, RIGHT, DOWN));
        symbolToDirection.put("┴", Set.of(UP, RIGHT, LEFT));
        symbolToDirection.put("┤", Set.of(UP, DOWN, LEFT));
        symbolToDirection.put("┼", Set.of(UP, RIGHT, DOWN, LEFT));

        for (Map.Entry<String, Set<Direction>> entry : symbolToDirection.entrySet()) {
            directionToSymbol.put(entry.getValue(), entry.getKey());
        }
    }

    /**
     * Creates a new Tile from a shape with the given Treasure.
     */
    public static Tile createTileFromShape(String pathShape, Treasure treasure) {
        if (symbolToDirection.containsKey(pathShape)) {
            Set<Direction> pathwayConnections = symbolToDirection.get(pathShape);
            return new Tile(pathwayConnections, treasure);
        }
        else {
            throw new IllegalArgumentException("Invalid path shape given.");
        }
    }

    public static Set<Direction> getDirectionsForSymbol(String pathShape) {
        return symbolToDirection.get(pathShape);
    }

    /**
     * Creates a new Tile from a shape.
     */
    public static Tile createTileFromShape(String pathShape) {
        return createTileFromShape(pathShape, createTreasure(Gem.alexandrite, Gem.alexandrite));
    }

    /**
     * Returns a String representation of the pathway connections within the Tile.
     */
    public static String createShapeFromTile(Tile tile) {
        Set<Direction> directions = new HashSet<>();
        if (tile.connects(UP)) {
            directions.add(UP);
        }
        if (tile.connects(DOWN)) {
            directions.add(DOWN);
        }
        if (tile.connects(LEFT)) {
            directions.add(LEFT);
        }
        if (tile.connects(RIGHT)) {
            directions.add(RIGHT);
        }
        return directionToSymbol.get(directions);
    }

    public static List<Position> standardizedPositionList(Set<Position> positionSet) {
        List<Position> positionList = new ArrayList<>(positionSet);
        positionList.sort(new Position.RowColumnOrderComparator());
        return positionList;
    }

    public static Treasure createTreasure(Gem gem1, Gem gem2) {
        List<Gem> gems = List.of(gem1, gem2);
        return new Treasure(gems);
    }
}
