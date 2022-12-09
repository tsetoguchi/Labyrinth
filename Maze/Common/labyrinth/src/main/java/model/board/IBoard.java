package model.board;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import model.Position;

/**
 * Represents Labyrinth's board, which is a grid of Tiles.
 */
public interface IBoard {

  /**
   * Retrieves the tile at the given position
   */
  Tile getTileAt(Position position);

  /**
   * Retrieves the spare tile of the board
   */
  Tile getSpareTile();

  /**
   * The height of the board
   */
  int getHeight();

  /**
   * The width of the board
   */
  int getWidth();

  /**
   * Slides the row (for left or right slides) or column (for up or down slides) at the given index
   * in the specified direction, then inserts the spare tile after rotating it the given number of
   * times
   *
   * @precondition isValidSlideAndInsert(direction, index, rotations) is called and returns True
   */
  void slideAndInsert(Direction direction, int index, int rotations);

  /**
   * Returns a set of all positions that can be reached via a continuous pathway from the given
   * starting position
   */
  Set<Position> getReachablePositions(Position startingPosition);

  /**
   * Gets the position of the first Tile (in row-column order) that matches the given predicate
   */
  Optional<Position> getFirstTileMatching(Predicate<Tile> searchFunction);

  /**
   * Get a read-only version of the board which can be used to test potential slide and insert
   * actions
   */
  ExperimentationBoard getExperimentationBoard();

  /**
   * Print the board as a grid of symbols representing the Tile pathways (no gems).
   */
  String toSymbolGrid();


  /**
   * Returns a deep copy of the board
   */
  IBoard deepCopy();
}
