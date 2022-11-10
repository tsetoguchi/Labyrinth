package game.model;

import game.exceptions.IllegalGameActionException;

import java.util.*;
import java.util.function.Predicate;

import static game.model.Direction.*;

/**
 * The standard Labyrinth board, which is 7x7.
 */
public class StandardBoard implements Board {
    private static final int WIDTH = 7;
    private static final int HEIGHT = 7;

    protected final Tile[][] tileGrid;

    protected Tile spareTile;

    public StandardBoard(Tile[][] tileGrid, Tile spareTile) {
        if (tileGrid.length != HEIGHT) {
            throw new IllegalArgumentException("Tried to create a board that had fewer than 7 rows.");
        }
        for (Tile[] tiles : tileGrid) {
            if (tiles.length != WIDTH) {
                throw new IllegalArgumentException("Tried to create a board where a row did not have exactly " +
                        "7 elements.");
            }
        }
        this.tileGrid = tileGrid;
        this.spareTile = spareTile;
    }

    @Override
    public Tile getTileAt(Position position) {
        if (position.getRow() >= tileGrid.length) {
            throw new IllegalArgumentException("Tried to access a tile on a row index that was not on the board.");
        }
        if (position.getColumn() >= tileGrid[0].length) {
            throw new IllegalArgumentException("Tried to access a tile on a column index that was not on the board.");
        }
        return tileGrid[position.getRow()][position.getColumn()];
    }

    @Override
    public Tile getSpareTile() {
        return spareTile;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    /**
     * Validate that sliding a row or column at the given index in the specified direction does not violate game
     * rules or pass invalid arguments.
     */
    @Override
    public boolean isValidSlideAndInsert(Direction direction, int index, int rotations) {
        boolean isEvenIndex = index % 2 == 0;
        boolean isPositiveIndex = index >= 0;
        boolean indexWithinBounds = ((direction == UP || direction == DOWN) && index < this.getHeight())
                              || ((direction == LEFT || direction == RIGHT) && index < this.getWidth());
        boolean isPositiveRotations = rotations >= 0;

        return isEvenIndex && isPositiveIndex && indexWithinBounds && isPositiveRotations;
    }

    /**
     * Slides the row (for left or right slides) or column (for up or down slides) at the given index in the specified
     * Direction, then inserts the spare tile after rotating it the given number of times.
     *
     * @precondition isValidSlideAndInsert(direction, index, rotations) is called and returns True
     */
    @Override
    public void slideAndInsert(Direction direction, int index, int rotations) {
        if (!this.isValidSlideAndInsert(direction, index, rotations)) {
            throw new IllegalGameActionException("Tried to perform an invalid slide and insert action.");
        }
        Tile nextSpareTile = slide(direction, index);
        Position emptySpot = this.getEmptySpot(direction, index);
        insert(rotations, emptySpot, nextSpareTile);
    }

    /**
     * Returns a set of all positions that can be reached via a continuous pathway from the given starting position.
     */
    @Override
    public Set<Position> getReachablePositions(Position startingPosition) {
        Set<Position> reachablePositions = new HashSet<>();

        Set<Position> explored = new HashSet<>();
        Queue<Position> frontier = new LinkedList<>();
        frontier.add(startingPosition);
        while (!frontier.isEmpty()) {
            Position next = frontier.poll();

            if (explored.contains(next)) {
                continue;
            } else {
                if (!next.equals(startingPosition)) {
                    reachablePositions.add(next);
                }
                explored.add(next);
            }

            Set<Position> neighbors = this.getReachableNeighbors(next);
            for (Position neighbor : neighbors) {
                if (!explored.contains(neighbor)) {
                    frontier.add(neighbor);
                }
            }
        }

        return reachablePositions;
    }

    /**
     * Gets the position of the first Tile (in row-column order) that matches the given predicate
     */
    @Override
    public Optional<Position> getFirstTileMatching(Predicate<Tile> searchFunction) {
        for (int rowIndex = 0; rowIndex < this.getHeight(); rowIndex++) {
            for (int colIndex = 0; colIndex < this.getWidth(); colIndex++) {
                Position tilePosition = new Position(rowIndex, colIndex);
                if (searchFunction.test(this.getTileAt(tilePosition))) {
                    return Optional.of(tilePosition);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Get a read-only version of the board which can be used to test potential slide and insert actions.
     */
    @Override
    public ExperimentationBoard getExperimentationBoard() {
        return new StandardExperimentationBoard(this);
    }

    @Override
    public String toSymbolGrid() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < this.tileGrid.length; row++) {
            for (Tile tile : this.tileGrid[row]) {
                builder.append(tile.toSymbol());
                builder.append(",");
            }
            if (row < this.tileGrid.length - 1) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    @Override
    public Board deepCopy() {
        Tile[][] newGrid = new Tile[this.getHeight()][this.getWidth()];

        for (int row = 0; row < newGrid.length; row++) {
            for (int col = 0; col < newGrid[row].length; col++) {
                newGrid[row][col] = (this.getTileAt(new Position(row, col)).deepCopy());
            }
        }
        return new StandardBoard(newGrid, this.spareTile.deepCopy());
    }

    /**
     * Slides the row (for left or right slides) or column (for up or down slides) at the given index in the specified
     * Direction and returns the Tile that was shifted off the board.
     */
    private Tile slide(Direction direction, int index) {
        Tile nextSpareTile = null;
        if (direction == LEFT || direction == RIGHT ){
            nextSpareTile = this.slideRow(direction, index);
        }
        if (direction == UP || direction == DOWN){
            nextSpareTile = this.slideColumn(direction, index);
        }
        return nextSpareTile;
    }

    /**
     * Rotates the current spare Tile by the given number of rotations, inserts it at the
     * given empty spot and sets the new spare Tile to be the given Tile.
     */
    private void insert(int rotations, Position emptySpot, Tile nextSpareTile) {
        this.spareTile.rotate(rotations);
        this.tileGrid[emptySpot.getRow()][emptySpot.getColumn()] = this.spareTile;
        this.spareTile = nextSpareTile;
    }

    /**
     * Slides the given row in the given direction and returns the tile that was pushed off the board.
     */
    private Tile slideRow(Direction direction, int rowIndex) {
        Tile[] row = this.tileGrid[rowIndex];
        Tile nextSpareTile = null;
        if (direction == LEFT) {
            for (int colIndex = 0; colIndex < row.length; colIndex++) {
                if (colIndex == 0) {
                    nextSpareTile = row[colIndex];
                }
                if (colIndex < row.length - 1) {
                    row[colIndex] = row[colIndex + 1];
                }
            }
        }
        else if (direction == RIGHT) {
            for (int colIndex = row.length - 1; colIndex >= 0; colIndex--) {
                if (colIndex == row.length - 1) {
                    nextSpareTile = row[colIndex];
                }
                if (colIndex > 0) {
                    row[colIndex] = row[colIndex - 1];
                }
            }
        }
        else {
            throw new IllegalArgumentException("Tried to shift a row up or down");
        }
        return nextSpareTile;
    }

    /**
     * For each tile adjacent to the tile at the given Position, checks whether the origin tile connects to the adjacent
     * tile and returns the Position of all such tiles.
     */
    private Set<Position> getReachableNeighbors(Position position) {
        final int MAX_ROW_INDEX = this.tileGrid.length - 1;
        final int MAX_COL_INDEX = this.tileGrid[0].length - 1;

        Set<Position> neighbors = new HashSet<>();

        Tile tile = getTileAt(position);
        if (tile.connects(UP) && position.getRow() > 0) {
            Position positionAbove = new Position(position.getRow() - 1, position.getColumn());
            if (getTileAt(positionAbove).connects(DOWN)) {
                neighbors.add(positionAbove);
            }
        }
        if (tile.connects(DOWN) && position.getRow() < MAX_ROW_INDEX) {
            Position positionBelow = new Position(position.getRow() + 1, position.getColumn());
            if (getTileAt(positionBelow).connects(UP)) {
                neighbors.add(positionBelow);
            }
        }
        if (tile.connects(LEFT) && position.getColumn() > 0) {
            Position positionLeft = new Position(position.getRow(), position.getColumn() - 1);
            if (getTileAt(positionLeft).connects(RIGHT)) {
                neighbors.add(positionLeft);
            }
        }
        if (tile.connects(RIGHT) && position.getColumn() < MAX_COL_INDEX) {
            Position positionRight = new Position(position.getRow(), position.getColumn() + 1);
            if (getTileAt(positionRight).connects(LEFT)) {
                neighbors.add(positionRight);
            }
        }

        return neighbors;
    }


    /**
     * Slides the given column in the given direction and returns the tile that was pushed off the board.
     */
    private Tile slideColumn(Direction direction, int colIndex) {
        int boardHeight = this.tileGrid.length;
        Tile nextSpareTile = null;
        if (direction == UP) {
            for (int rowIndex = 0; rowIndex < boardHeight; rowIndex++) {
                if (rowIndex == 0) {
                    nextSpareTile = this.tileGrid[rowIndex][colIndex];
                }
                if (rowIndex < boardHeight - 1) {
                    this.tileGrid[rowIndex][colIndex] = this.tileGrid[rowIndex + 1][colIndex];
                }
            }
        }
        else if (direction == DOWN) {
            for (int rowIndex = boardHeight - 1; rowIndex >= 0; rowIndex--) {
                if (rowIndex == boardHeight - 1) {
                    nextSpareTile = this.tileGrid[rowIndex][colIndex];
                }
                if (rowIndex > 0) {
                    this.tileGrid[rowIndex][colIndex] = this.tileGrid[rowIndex - 1][colIndex];
                }
            }
        }
        else {
            throw new IllegalArgumentException("Tried to shift a column left or right");
        }
        return nextSpareTile;
    }

    /**
     * Find the position of the empty tile when the row or column after sliding the given index in the given direction.
     */
    private Position getEmptySpot(Direction direction, int index) {
        if (direction == UP) {
            return new Position(tileGrid.length - 1, index);
        }
        if (direction == DOWN) {
            return new Position(0, index);
        }
        if (direction == LEFT) {
            return new Position(index, tileGrid[index].length - 1);
        }
        if (direction == RIGHT) {
            return new Position(index, 0);
        }
        else {
            throw new IllegalArgumentException("Unknown direction while getting empty spot.");
        }
    }
}
