package game.model;

public class FlexibleBoard extends AbstractBoard {


  public FlexibleBoard(Tile[][] tileGrid, Tile spareTile, int width, int height) {
    super(width, height, tileGrid, spareTile, new FlexibleDefaultRules(width, height));
  }

  public FlexibleBoard(int width, int height, Tile[][] tileGrid, Tile spareTile, IRules rules) {
    super(width, height, tileGrid, spareTile, rules);
  }

  public FlexibleBoard(int width, int height) {
    super(width, height);
  }

  @Override
  public IBoard deepCopy() {
    Tile[][] newGrid = new Tile[this.getHeight()][this.getWidth()];

    for (int row = 0; row < newGrid.length; row++) {
      for (int col = 0; col < newGrid[row].length; col++) {
        newGrid[row][col] = (this.getTileAt(new Position(row, col)).deepCopy());
      }
    }
    return new FlexibleBoard(newGrid, this.spareTile.deepCopy(), super.width, super.height);
  }
}

