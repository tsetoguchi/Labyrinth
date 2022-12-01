package model;

import model.board.Direction;
import model.board.Gem;
import model.board.IBoard;
import model.board.Tile;
import model.board.Treasure;
import referee.IRules;

import java.awt.*;
import java.util.*;
import java.util.List;

import static model.board.Direction.*;

public class Utils {
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


    public static Tile[][] generateRandomTileGrid() {
        Tile[][] tileGrid = new Tile[7][7];
        List<Gem> randomGems = Arrays.asList(Gem.values());
        randomGems.remove(Gem.hackmanite); // we will use this for the spare tile
        Collections.shuffle(randomGems);

        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                tileGrid[row][col] = generateRandomTile(randomGems.get(row), randomGems.get(col));
            }
        }
        return tileGrid;
    }

    public static Tile generateRandomTile(Gem firstGem, Gem secondGem) {
        Random rand = new Random();
        Set<Direction> directions = new HashSet<>();
        for (Direction direction : Direction.values()) {
            if (rand.nextBoolean()) {
                directions.add(direction);
            }
        }
        return new Tile(directions, new Treasure(List.of(firstGem, secondGem)));
    }

    public static List<Position> immovablePositionsForBoard(IBoard board, IRules rules) {
        List<Position> immovablePositions = new ArrayList<>();
        // Iterate through immovable rows
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Position currentPosition = new Position(row, col);
                if (rules.immovablePosition(currentPosition)) {
                    immovablePositions.add(currentPosition);
                }
            }
        }
        return immovablePositions;
    }

    private static List<Color> generateUniqueColors(int numberOfColors) {
        Set<Color> colors = new HashSet<>();
        Random random = new Random();
        while (colors.size() < numberOfColors) {
            colors.add(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        }

        return new ArrayList<>(colors);
    }

}
