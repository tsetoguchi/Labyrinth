package game.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    public void testEquals() {
        Position positionA = new Position(5,5);
        Position alsoPositionA = new Position(5, 5);
        Position differentPosition = new Position(2, 6);
        Position differentPosition2 = new Position(5, 4);

        assertEquals(positionA, positionA);
        assertEquals(alsoPositionA, positionA);
        assertNotEquals(differentPosition, positionA);
        assertNotEquals(differentPosition2, positionA);
    }

    @Test
    public void testCreatePositionWithPositiveIndices() {
        assertDoesNotThrow(() -> new Position(0, 0));
        assertDoesNotThrow(() -> new Position(1, 6));
        assertDoesNotThrow(() -> new Position(20, 60));
    }

    @Test
    public void testCreatePositionWithNegativeIndices() {
        assertThrows(IllegalArgumentException.class, () -> new Position(-1, -1));
        assertThrows(IllegalArgumentException.class, () -> new Position(3, -5));
    }

    @Test
    public void testGetRow() {
        Position position = new Position(2, 5);
        assertEquals(2, position.getRow());
    }

    @Test
    public void testGetColumn() {
        Position position = new Position(2, 5);
        assertEquals(5, position.getColumn());
    }

    @Test
    public void testAddDeltaWithoutWrapping() {
        Position position = new Position(0, 0);
        position = position.addDeltaWithBoardWrap(0, 1, 7, 7);
        assertEquals(0, position.getRow());
        assertEquals(1, position.getColumn());

        position = position.addDeltaWithBoardWrap(1, 0, 7, 7);
        assertEquals(1, position.getRow());
        assertEquals(1, position.getColumn());

        position = position.addDeltaWithBoardWrap(5, 5, 7, 7);
        assertEquals(6, position.getRow());
        assertEquals(6, position.getColumn());

        position = position.addDeltaWithBoardWrap(-5, -2, 7, 7);
        assertEquals(1, position.getRow());
        assertEquals(4, position.getColumn());
    }

    @Test
    public void testAddDeltaWithWrapping() {
        Position positionTopLeft = new Position(0,0);
        Position positionBottomRight = new Position(6, 6);

        positionTopLeft = positionTopLeft.addDeltaWithBoardWrap(7, 7, 7, 7);
        assertEquals(0, positionTopLeft.getRow());
        assertEquals(0, positionTopLeft.getColumn());

        positionTopLeft = positionTopLeft.addDeltaWithBoardWrap(-1, 0 ,7, 7);
        assertEquals(6, positionTopLeft.getRow());
        assertEquals(0, positionTopLeft.getColumn());

        positionTopLeft = positionTopLeft.addDeltaWithBoardWrap(0, -1 ,7, 7);
        assertEquals(6, positionTopLeft.getRow());
        assertEquals(6, positionTopLeft.getColumn());

        positionBottomRight = positionBottomRight.addDeltaWithBoardWrap(2, 2, 7, 7);
        assertEquals(1, positionBottomRight.getRow());
        assertEquals(1, positionBottomRight.getColumn());

        positionBottomRight = positionBottomRight.addDeltaWithBoardWrap(-7, -7, 7, 7);
        assertEquals(1, positionBottomRight.getRow());
        assertEquals(1, positionBottomRight.getColumn());
    }
}
