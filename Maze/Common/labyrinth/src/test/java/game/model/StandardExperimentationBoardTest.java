package game.model;

import game.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static game.model.Direction.*;
import static org.junit.jupiter.api.Assertions.*;

public class StandardExperimentationBoardTest {
    private StandardExperimentationBoard exBoard;

    @BeforeEach
    public void setup() {
        StandardBoard standardBoard = TestUtils.createUniformBoard(false, false, true, true,
                TestUtils.makeTile(true, true, false, false,
                        TestUtils.makeTreasure(Gem.ammolite, Gem.labradorite)));
        exBoard = new StandardExperimentationBoard(standardBoard);
    }

    @Test
    public void testFindReachableTilePositionsAfterSlideAndInsert() {
        Set<Position> reachable1 = exBoard.findReachableTilePositionsAfterSlideAndInsert(DOWN, 2, 0, new Position(0, 0));
        assertEquals(1, reachable1.size());
        assertTrue(reachable1.contains(new Position(0, 1)));

        Set<Position> reachable2 = exBoard.findReachableTilePositionsAfterSlideAndInsert(LEFT, 6, 1, new Position(6, 4));
        assertEquals(6, reachable2.size());
        assertFalse(reachable2.contains(new Position(6, 3)));
        for (int i = 0; i < 7; i++) {
            if (i != 3) {
                assertTrue(reachable2.contains(new Position(6, i)));
            }
        }
    }
}
