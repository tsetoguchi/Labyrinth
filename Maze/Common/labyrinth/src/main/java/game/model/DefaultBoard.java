package game.model;

/**
 * The standard 7x7 Labyrinth board.
 */
public class DefaultBoard extends AbstractBoard {

  private static final int WIDTH = 7;
  private static final int HEIGHT = 7;

  public DefaultBoard(Tile[][] tileGrid, Tile spareTile) {
    super(WIDTH, HEIGHT, tileGrid, spareTile, new DefaultRules());
  }

  @Override
  public IBoard deepCopy() {
    Tile[][] newGrid = new Tile[this.getHeight()][this.getWidth()];

    for (int row = 0; row < newGrid.length; row++) {
      for (int col = 0; col < newGrid[row].length; col++) {
        newGrid[row][col] = (this.getTileAt(new Position(row, col)).deepCopy());
      }
    }
    return new DefaultBoard(newGrid, this.spareTile.deepCopy());
  }
}