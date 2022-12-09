package model.board;

import static model.board.Direction.DOWN;
import static model.board.Direction.LEFT;
import static model.board.Direction.RIGHT;
import static model.board.Direction.UP;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import model.Position;

/**
 * Represents a board in Labyrinth
 */
public class Board implements IBoard {

  protected final Tile[][] tileGrid;

  protected Tile spareTile;

  protected final int width;

  protected final int height;


  public Board(int width, int height, Tile[][] tileGrid, Tile spareTile) {
    this.width = width;
    this.height = height;
    if (tileGrid.length != height) {
      throw new IllegalArgumentException("Tried to create a board with an invalid number of rows.");
    }
    for (Tile[] tiles : tileGrid) {
      if (tiles.length != width) {
        throw new IllegalArgumentException(
            "Invalid number of columns.");
      }
    }
    this.tileGrid = tileGrid;
    this.spareTile = spareTile;
  }

  public Board(int width, int height) {
    this.width = width;
    this.height = height;

    Gem[] gemPoolArray = Gem.values();
    List<Gem> gemPool = new ArrayList<>(List.of(gemPoolArray));
    this.tileGrid = this.generateRandomBoard(width, height, gemPool);
    Treasure spareTileTreasure = new Treasure(gemPool.get(0), gemPool.get(0));
    this.spareTile = new Tile(spareTileTreasure);
  }

  /**
   * Generates a random tile grid based on the given width and height. The tile grid will also
   * contain random gems from the given gem pool.
   */
  private Tile[][] generateRandomBoard(int width, int height, List<Gem> gemPool) {
    Tile[][] tileGrid = new Tile[height][width];

    List<Gem> rowGems = new ArrayList<>();
    List<Gem> colGems = new ArrayList<>();

    for (int i = 0; i < height; i++) {
      int poolIndex = ThreadLocalRandom.current().nextInt(0, gemPool.size());
      rowGems.add(gemPool.get(poolIndex));
      gemPool.remove(poolIndex);
    }
    for (int i = 0; i < width; i++) {
      int poolIndex = ThreadLocalRandom.current().nextInt(0, gemPool.size());
      colGems.add(gemPool.get(poolIndex));
      gemPool.remove(poolIndex);
    }
    for (int row = 0; row < tileGrid.length; row++) {
      for (int col = 0; col < tileGrid[0].length; col++) {
        Treasure treasure = new Treasure(rowGems.get(row), colGems.get(col));
        tileGrid[row][col] = new Tile(treasure);
      }
    }
    return tileGrid;
  }

  /**
   * Retrieve the tile at the given position.
   */
  @Override
  public Tile getTileAt(Position position) {
    if (position.getRow() >= this.tileGrid.length) {
      throw new IllegalArgumentException(
          "Tried to access a tile on a row index that was not on the board.");
    }
    if (position.getColumn() >= this.tileGrid[0].length) {
      throw new IllegalArgumentException(
          "Tried to access a tile on a column index that was not on the board.");
    }
    return this.tileGrid[position.getRow()][position.getColumn()];
  }

  /**
   * Retrieve the current spare tile.
   */
  @Override
  public Tile getSpareTile() {
    return this.spareTile;
  }

  /**
   * Returns the height of the board.
   */
  @Override
  public int getHeight() {
    return this.height;
  }

  /**
   * Returns the width of the board.
   */
  @Override
  public int getWidth() {
    return this.width;
  }

  /**
   * Apply a slide and insert to the board, with the given direction, row/col index, and clockwise
   * rotation of spare tile.
   */
  @Override
  public void slideAndInsert(Direction direction, int index, int rotations) {
    Tile nextSpareTile = this.slide(direction, index);
    Position emptySpot = this.getEmptySpot(direction, index);
    this.insert(rotations, emptySpot, nextSpareTile);
  }

  /**
   * Returns a set of all positions that can be reached via a continuous pathway from the given
   * starting position.
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
   * Get a read-only version of the board which can be used to test potential slide and insert
   * actions.
   */
  @Override
  public ExperimentationBoard getExperimentationBoard() {
    Tile[][] newGrid = new Tile[this.getHeight()][this.getWidth()];
    for (int row = 0; row < newGrid.length; row++) {
      for (int col = 0; col < newGrid[row].length; col++) {
        newGrid[row][col] = (this.getTileAt(new Position(row, col)).deepCopy());
      }
    }
    return new ExperimentationBoard(this.width, this.height, newGrid, this.spareTile.deepCopy());
  }

  /**
   * Convert the board into its canonical characters as a string
   */
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


  /**
   * Returns a deep copy of the board
   */
  public IBoard deepCopy() {
    Tile[][] newGrid = new Tile[this.getHeight()][this.getWidth()];
    for (int row = 0; row < newGrid.length; row++) {
      for (int col = 0; col < newGrid[row].length; col++) {
        newGrid[row][col] = (this.getTileAt(new Position(row, col)).deepCopy());
      }
    }
    return new Board(this.width,
        this.height, newGrid, this.spareTile.deepCopy());
  }


  /**
   * Slides the row (for left or right slides) or column (for up or down slides) at the given index
   * in the specified Direction and returns the Tile that was shifted off the board.
   */
  private Tile slide(Direction direction, int index) {
    Tile nextSpareTile = null;
    if (direction == LEFT || direction == RIGHT) {
      nextSpareTile = this.slideRow(direction, index);
    }
    if (direction == UP || direction == DOWN) {
      nextSpareTile = this.slideColumn(direction, index);
    }
    return nextSpareTile;
  }

  /**
   * Rotates the current spare Tile by the given number of rotations, inserts it at the given empty
   * spot and sets the new spare Tile to be the given Tile.
   */
  private void insert(int rotations, Position emptySpot, Tile nextSpareTile) {
    this.spareTile.rotate(rotations);
    this.tileGrid[emptySpot.getRow()][emptySpot.getColumn()] = this.spareTile;
    this.spareTile = nextSpareTile;
  }

  /**
   * Slides the given row in the given direction and returns the tile that was pushed off the
   * board.
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
    } else if (direction == RIGHT) {
      for (int colIndex = row.length - 1; colIndex >= 0; colIndex--) {
        if (colIndex == row.length - 1) {
          nextSpareTile = row[colIndex];
        }
        if (colIndex > 0) {
          row[colIndex] = row[colIndex - 1];
        }
      }
    } else {
      throw new IllegalArgumentException("Tried to shift a row up or down");
    }
    return nextSpareTile;
  }

  /**
   * For each tile adjacent to the tile at the given Position, checks whether the origin tile
   * connects to the adjacent tile and returns the Position of all such tiles.
   */
  private Set<Position> getReachableNeighbors(Position position) {
    final int MAX_ROW_INDEX = this.tileGrid.length - 1;
    final int MAX_COL_INDEX = this.tileGrid[0].length - 1;

    Set<Position> neighbors = new HashSet<>();

    Tile tile = this.getTileAt(position);
    if (tile.connects(UP) && position.getRow() > 0) {
      Position positionAbove = new Position(position.getRow() - 1, position.getColumn());
      if (this.getTileAt(positionAbove).connects(DOWN)) {
        neighbors.add(positionAbove);
      }
    }
    if (tile.connects(DOWN) && position.getRow() < MAX_ROW_INDEX) {
      Position positionBelow = new Position(position.getRow() + 1, position.getColumn());
      if (this.getTileAt(positionBelow).connects(UP)) {
        neighbors.add(positionBelow);
      }
    }
    if (tile.connects(LEFT) && position.getColumn() > 0) {
      Position positionLeft = new Position(position.getRow(), position.getColumn() - 1);
      if (this.getTileAt(positionLeft).connects(RIGHT)) {
        neighbors.add(positionLeft);
      }
    }
    if (tile.connects(RIGHT) && position.getColumn() < MAX_COL_INDEX) {
      Position positionRight = new Position(position.getRow(), position.getColumn() + 1);
      if (this.getTileAt(positionRight).connects(LEFT)) {
        neighbors.add(positionRight);
      }
    }

    return neighbors;
  }


  /**
   * Slides the given column in the given direction and returns the tile that was pushed off the
   * board.
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
    } else if (direction == DOWN) {
      for (int rowIndex = boardHeight - 1; rowIndex >= 0; rowIndex--) {
        if (rowIndex == boardHeight - 1) {
          nextSpareTile = this.tileGrid[rowIndex][colIndex];
        }
        if (rowIndex > 0) {
          this.tileGrid[rowIndex][colIndex] = this.tileGrid[rowIndex - 1][colIndex];
        }
      }
    } else {
      throw new IllegalArgumentException("Tried to shift a column left or right");
    }
    return nextSpareTile;
  }

  /**
   * Find the position of the empty tile when the row or column after sliding the given index in the
   * given direction.
   */
  private Position getEmptySpot(Direction direction, int index) {
    if (direction == UP) {
      return new Position(this.tileGrid.length - 1, index);
    }
    if (direction == DOWN) {
      return new Position(0, index);
    }
    if (direction == LEFT) {
      return new Position(index, this.tileGrid[index].length - 1);
    }
    if (direction == RIGHT) {
      return new Position(index, 0);
    } else {
      throw new IllegalArgumentException("Unknown direction while getting empty spot.");
    }
  }

  public String toString() {
    String result = " 0123456\n";
    for (int row = 0; row < this.getHeight(); row++) {

      result += row;
      for (int col = 0; col < this.getWidth(); col++) {
        result += this.tileGrid[row][col].toSymbol();
      }
      result += "\n";
    }
    return result;
  }

}
