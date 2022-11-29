package game;

import game.model.*;
import game.model.projections.ExperimentationBoardProjection;

import static game.model.Direction.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

public class TestUtils {

  public static DefaultBoard NON_UNIFORM_BOARD;

  public static Gem[] rowGems = {Gem.alexandrite, Gem.mexican_opal, Gem.aplite, Gem.apatite,
      Gem.hackmanite, Gem.beryl, Gem.orange_radiant};
  public static Gem[] colGems = {Gem.heliotrope, Gem.hematite, Gem.garnet, Gem.labradorite,
      Gem.lapis_lazuli, Gem.magnesite, Gem.moonstone};

  static {
    NON_UNIFORM_BOARD = createNonUniformBoard();
  }

  public static DefaultBoard createUniformBoard(boolean up, boolean down, boolean left,
                                                boolean right) {
    Tile[][] grid = new Tile[7][7];
    for (int rowIndex = 0; rowIndex < 7; rowIndex++) {
      for (int colIndex = 0; colIndex < 7; colIndex++) {
        grid[rowIndex][colIndex] = makeTile(up, down, left, right,
            makeTreasure(rowGems[rowIndex], colGems[colIndex]));
      }
    }
    DefaultBoard board = new DefaultBoard(grid,
        makeTile(up, down, left, right, makeTreasure(Gem.ammolite, Gem.mexican_opal)));
    return board;
  }

  public static DefaultBoard createUniformBoard(boolean up, boolean down, boolean left,
                                                boolean right, Tile spare) {
    Tile[][] grid = new Tile[7][7];
    for (int rowIndex = 0; rowIndex < 7; rowIndex++) {
      for (int colIndex = 0; colIndex < 7; colIndex++) {
        grid[rowIndex][colIndex] = makeTile(up, down, left, right,
            makeTreasure(rowGems[rowIndex], colGems[colIndex]));
      }
    }
    return new DefaultBoard(grid, spare);
  }

  public static DefaultBoard createNonUniformBoard() {

    Tile[][] grid = new Tile[7][7];

    for (int rowIndex = 0; rowIndex < 7; rowIndex++) {
      for (int colIndex = 0; colIndex < 7; colIndex++) {
        grid[rowIndex][colIndex] = makeTile(false, false, true, true,
            makeTreasure(rowGems[rowIndex], colGems[colIndex]));
      }
    }

    grid[0][0] = makeTile(true, false, true, false,
        makeTreasure(Gem.ammolite, Gem.labradorite));
    grid[0][1] = makeTile(false, true, true, false,
        makeTreasure(Gem.ammolite, Gem.lapis_lazuli));
    grid[0][2] = makeTile(true, false, true, false,
        makeTreasure(Gem.ammolite, Gem.jasper));
    grid[0][3] = makeTile(false, false, true, true,
        makeTreasure(Gem.ammolite, Gem.jaspilite));
    grid[0][4] = makeTile(true, true, false, false,
        makeTreasure(Gem.ammolite, Gem.goldstone));
    grid[0][5] = makeTile(true, true, false, false,
        makeTreasure(Gem.ammolite, Gem.fancy_spinel_marquise));
    grid[0][6] = makeTile(true, true, false, false,
        makeTreasure(Gem.ammolite, Gem.stilbite));

    DefaultBoard board = new DefaultBoard(grid,
        makeTile(false, false, true, true, makeTreasure(Gem.ammolite, Gem.mexican_opal)));
    return board;
  }

  public static DefaultBoard createNonUniformBoardWithSomeUniqueTreasures() {

    Tile[][] grid = new Tile[7][7];
    grid[0][0] = makeTile(true, false, false, false,
        makeTreasure(Gem.ammolite, Gem.mexican_opal));
    grid[0][1] = makeTile(false, true, false, false,
        makeTreasure(Gem.ammolite, Gem.mexican_opal));
    grid[0][2] = makeTile(false, false, true, false,
        makeTreasure(Gem.ammolite, Gem.mexican_opal));
    grid[0][3] = makeTile(false, false, false, true,
        makeTreasure(Gem.ammolite, Gem.mexican_opal));
    grid[0][4] = makeTile(false, false, false, false,
        makeTreasure(Gem.ammolite, Gem.mexican_opal));
    grid[0][5] = makeTile(false, false, false, false,
        makeTreasure(Gem.ammolite, Gem.mexican_opal));
    grid[0][6] = makeTile(false, false, false, false,
        makeTreasure(Gem.ammolite, Gem.mexican_opal));

    for (int rowIndex = 0; rowIndex < 7; rowIndex++) {
      for (int colIndex = 0; colIndex < 7; colIndex++) {
        grid[rowIndex][colIndex] = makeTile(false, false, false, false,
            makeTreasure(Gem.ammolite, Gem.mexican_opal));
      }
    }
    DefaultBoard board = new DefaultBoard(grid,
        makeTile(false, false, true, true, makeTreasure(Gem.ammolite, Gem.mexican_opal)));
    return board;
  }

  public static Treasure makeTreasure(Gem gem1, Gem gem2) {
    List<Gem> gems = new ArrayList<>();
    gems.add(gem1);
    gems.add(gem2);

    return new Treasure(gems);
  }

  public static Tile makeTile(boolean up, boolean down, boolean left, boolean right,
      Treasure treasure) {
    Set<Direction> pathwayConnections = new HashSet<>();
    if (up) {
      pathwayConnections.add(UP);
    }
    if (down) {
      pathwayConnections.add(DOWN);
    }
    if (left) {
      pathwayConnections.add(LEFT);
    }
    if (right) {
      pathwayConnections.add(RIGHT);
    }
    return new Tile(pathwayConnections, treasure);
  }


  public static Treasure getTreasureAt(ExperimentationBoardProjection board, Position position) {
    Tile tile = board.getTileAt(position);
    return tile.getTreasure();
  }

  public static void assertTileShapeMatches(Tile tile, boolean up, boolean down, boolean left,
      boolean right) {
    assertEquals(up, tile.connects(UP));
    assertEquals(down, tile.connects(DOWN));
    assertEquals(left, tile.connects(LEFT));
    assertEquals(right, tile.connects(RIGHT));
  }

  public static void assertTileShapeMatches(Tile tile1, Tile tile2) {
    assertEquals(tile1.connects(UP), tile2.connects(UP));
    assertEquals(tile1.connects(DOWN), tile2.connects(DOWN));
    assertEquals(tile1.connects(LEFT), tile2.connects(LEFT));
    assertEquals(tile1.connects(RIGHT), tile2.connects(RIGHT));
  }
}
