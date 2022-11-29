package game.model;

import game.TestUtils;
import game.Exceptions.IllegalGameActionException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static game.model.Direction.*;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultIBoardTest {

    @Test
    public void testCreatingBoardLargerThan7by7() {
        Tile[][] grid = new Tile[8][8];
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            for (int colIndex = 0; colIndex < 8; colIndex++) {
                grid[rowIndex][colIndex] = new Tile(true, true, true, true,
                        new Treasure(Gem.ammolite, Gem.mexican_opal));
            }
        }

        assertThrows(IllegalArgumentException.class, () -> new DefaultBoard(grid,
                new Tile(true, true, true, true, new Treasure(Gem.ammolite, Gem.mexican_opal))));
    }

    @Test
    public void testCreatingBoardSmallerThan7by7() {
        Tile[][] grid = new Tile[5][5];
        for (int rowIndex = 0; rowIndex < 5; rowIndex++) {
            for (int colIndex = 0; colIndex < 5; colIndex++) {
                grid[rowIndex][colIndex] = new Tile(true, true, true, true,
                        new Treasure(Gem.ammolite, Gem.mexican_opal));
            }
        }

        assertThrows(IllegalArgumentException.class, () -> new DefaultBoard(grid,
                new Tile(true, true, true, true, new Treasure(Gem.ammolite, Gem.mexican_opal))));
    }

    @Test
    public void testGetTileAtValidPosition() {
        DefaultBoard board = TestUtils.createUniformBoard(true, true, true, true);

        TestUtils.assertTileShapeMatches(board.getTileAt(new Position(0, 0)), true, true, true, true);
    }

    @Test
    public void testGetTileAtInvalidPosition() {
        DefaultBoard board = TestUtils.createUniformBoard(true, true, true, true);

        assertThrows(IllegalArgumentException.class, () -> board.getTileAt(new Position(7,7)));
        assertThrows(IllegalArgumentException.class, () -> board.getTileAt(new Position(2,15)));
        assertThrows(IllegalArgumentException.class, () -> board.getTileAt(new Position(15,3)));
    }
    
    @Test
    public void testGetReachablePositions() {
        DefaultBoard horizontalBoard = TestUtils.createUniformBoard(false, false, true, true);
        Set<Position> horizontalReachableFromTopLeft 
                = horizontalBoard.getReachablePositions(new Position(0, 0));

        assertTrue(horizontalReachableFromTopLeft.contains(new Position(0,1)));
        assertTrue(horizontalReachableFromTopLeft.contains(new Position(0,2)));
        assertTrue(horizontalReachableFromTopLeft.contains(new Position(0,3)));
        assertTrue(horizontalReachableFromTopLeft.contains(new Position(0,4)));
        assertTrue(horizontalReachableFromTopLeft.contains(new Position(0,5)));
        assertTrue(horizontalReachableFromTopLeft.contains(new Position(0,6)));
        
        DefaultBoard pathlessBoard = TestUtils.createUniformBoard(true, false, true, false);
        Set<Position> pathlessReachable = pathlessBoard.getReachablePositions(new Position(3,3));
        assertEquals(0, pathlessReachable.size());
        
        DefaultBoard latticeBoard = TestUtils.createUniformBoard(true, true, true, true);
        Set<Position> allReachable = latticeBoard.getReachablePositions(new Position(0,0));
        assertFalse(allReachable.contains(new Position(0, 0)));
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                if (!(row == 0 && col == 0)) {
                    assertTrue(allReachable.contains(new Position(row, col)));
                }
            }
        }
    }

    @Test
    public void testInsertRotation() {
        DefaultBoard board = TestUtils.createUniformBoard(false, false, true, true);
        assertTrue(board.getRules().isValidSlideAndInsert(RIGHT, 0,0));
        board.slideAndInsert(RIGHT, 0, 0);
        Tile insertedTile = board.getTileAt(new Position(0,0));
        TestUtils.assertTileShapeMatches(insertedTile, false, false, true, true);

        assertTrue(board.getRules().isValidSlideAndInsert(RIGHT, 0,1));
        board.slideAndInsert(RIGHT, 0, 1);
        Tile insertedTile2 = board.getTileAt(new Position(0,0));
        TestUtils.assertTileShapeMatches(insertedTile2, true, true, false, false);
    }

    @Test
    public void testSlideRowLeft() {
        DefaultBoard board = TestUtils.createNonUniformBoard();
        assertTrue(board.getRules().isValidSlideAndInsert(LEFT, 0,0));
        board.slideAndInsert(LEFT, 0, 0);
        Tile tileAtIndex0 = board.getTileAt(new Position(0, 0));
        Tile tileAtIndex1 = board.getTileAt(new Position(0, 1));
        Tile tileAtIndex2 = board.getTileAt(new Position(0, 2));
        Tile tileAtIndex3 = board.getTileAt(new Position(0, 3));
        Tile tileAtIndex4 = board.getTileAt(new Position(0, 4));
        Tile tileAtIndex5 = board.getTileAt(new Position(0, 5));
        Tile tileAtIndex6 = board.getTileAt(new Position(0, 6));

        assertEquals(tileAtIndex0, TestUtils.NON_UNIFORM_BOARD.getTileAt(new Position(0, 1)));
        assertEquals(tileAtIndex1, TestUtils.NON_UNIFORM_BOARD.getTileAt(new Position(0, 2)));
        assertEquals(tileAtIndex2, TestUtils.NON_UNIFORM_BOARD.getTileAt(new Position(0, 3)));
        assertEquals(tileAtIndex3, TestUtils.NON_UNIFORM_BOARD.getTileAt(new Position(0, 4)));
        assertEquals(tileAtIndex4, TestUtils.NON_UNIFORM_BOARD.getTileAt(new Position(0, 5)));
        assertEquals(tileAtIndex5, TestUtils.NON_UNIFORM_BOARD.getTileAt(new Position(0, 6)));
        assertEquals(tileAtIndex6, TestUtils.NON_UNIFORM_BOARD.getSpareTile());
    }

    @Test
    public void testSlideRowRight() {
        DefaultBoard board = TestUtils.createNonUniformBoard();
        assertTrue(board.getRules().isValidSlideAndInsert(RIGHT, 0,7));
        board.slideAndInsert(RIGHT, 0, 7);
        Tile tileAtIndex0 = board.getTileAt(new Position(0, 0));
        Tile tileAtIndex1 = board.getTileAt(new Position(0, 1));
        Tile tileAtIndex2 = board.getTileAt(new Position(0, 2));
        Tile tileAtIndex3 = board.getTileAt(new Position(0, 3));
        Tile tileAtIndex4 = board.getTileAt(new Position(0, 4));
        Tile tileAtIndex5 = board.getTileAt(new Position(0, 5));
        Tile tileAtIndex6 = board.getTileAt(new Position(0, 6));

        assertEquals(tileAtIndex0, TestUtils.NON_UNIFORM_BOARD.getSpareTile());
        assertEquals(tileAtIndex1, TestUtils.NON_UNIFORM_BOARD.getTileAt(new Position(0, 0)));
        assertEquals(tileAtIndex2, TestUtils.NON_UNIFORM_BOARD.getTileAt(new Position(0, 1)));
        assertEquals(tileAtIndex3, TestUtils.NON_UNIFORM_BOARD.getTileAt(new Position(0, 2)));
        assertEquals(tileAtIndex4, TestUtils.NON_UNIFORM_BOARD.getTileAt(new Position(0, 3)));
        assertEquals(tileAtIndex5, TestUtils.NON_UNIFORM_BOARD.getTileAt(new Position(0, 4)));
        assertEquals(tileAtIndex6, TestUtils.NON_UNIFORM_BOARD.getTileAt(new Position(0, 5)));

        TestUtils.assertTileShapeMatches(tileAtIndex0, true, true, false, false);
    }

    @Test
    public void testSlideColumnDown() {
        DefaultBoard board = TestUtils.createNonUniformBoard();
        assertTrue(board.getRules().isValidSlideAndInsert(DOWN, 0,0));
        board.slideAndInsert(DOWN, 0, 0);

        Tile tileAtIndex0 = board.getTileAt(new Position(0, 0));
        Tile tileAtIndex1 = board.getTileAt(new Position(1, 0));

        assertEquals(tileAtIndex0, TestUtils.NON_UNIFORM_BOARD.getSpareTile());

        assertEquals(tileAtIndex1, TestUtils.NON_UNIFORM_BOARD.getTileAt(new Position(0, 0)));
    }

    @Test
    public void testSlideColumnUp() {
        DefaultBoard board = TestUtils.createNonUniformBoard();
        assertTrue(board.getRules().isValidSlideAndInsert(UP, 0,0));
        board.slideAndInsert(UP, 0, 0);
        assertTrue(board.getRules().isValidSlideAndInsert(UP, 0,0));
        board.slideAndInsert(UP, 0, 0);

        Tile tileAtIndex00 = board.getTileAt(new Position(0, 0));
        Tile tileAtIndex50 = board.getTileAt(new Position(5, 0));
        Tile tileAtIndex60 = board.getTileAt(new Position(6, 0));

        assertEquals(tileAtIndex00, TestUtils.NON_UNIFORM_BOARD.getTileAt(new Position(2, 0)));
        assertEquals(tileAtIndex50, TestUtils.NON_UNIFORM_BOARD.getSpareTile());
        assertEquals(tileAtIndex60, TestUtils.NON_UNIFORM_BOARD.getTileAt(new Position(0, 0)));

        assertTrue(board.getRules().isValidSlideAndInsert(UP, 2,3));
        board.slideAndInsert(UP, 2, 3);

        Tile tileAtIndex02 = board.getTileAt(new Position(0, 2));
        Tile tileAtIndex62 = board.getTileAt(new Position(6, 2));

        assertEquals(tileAtIndex02, TestUtils.NON_UNIFORM_BOARD.getTileAt(new Position(1, 2)));

        TestUtils.assertTileShapeMatches(tileAtIndex62, true, true, false, false);
    }

    @Test
    public void testInvalidSlideAndInsert() {
        DefaultBoard board = TestUtils.createNonUniformBoard();
        assertFalse(board.getRules().isValidSlideAndInsert(UP, -1,0));
        assertThrows(IllegalGameActionException.class, () -> board.slideAndInsert(UP, -1,0));
        assertFalse(board.getRules().isValidSlideAndInsert(RIGHT, -10,0));
        assertThrows(IllegalGameActionException.class, () -> board.slideAndInsert(RIGHT, -10,0));
        assertFalse(board.getRules().isValidSlideAndInsert(LEFT, 0,-1));
        assertThrows(IllegalGameActionException.class, () -> board.slideAndInsert(LEFT, 0,-1));
        assertFalse(board.getRules().isValidSlideAndInsert(UP, 1,0));
        assertThrows(IllegalGameActionException.class, () -> board.slideAndInsert(UP, 1,0));
        assertFalse(board.getRules().isValidSlideAndInsert(DOWN, 1,12));
        assertThrows(IllegalGameActionException.class, () -> board.slideAndInsert(DOWN, 1,12));
    }
}
