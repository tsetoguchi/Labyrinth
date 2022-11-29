package model.model;

import model.TestUtils;
import model.board.Gem;
import model.board.Tile;
import model.board.Treasure;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static model.board.Direction.*;
import static org.junit.jupiter.api.Assertions.*;

public class TileTest {
    @Test
    public void testConnectsAccuratelyDescribesPathways() {
        Tile tile1 = new Tile(true, false, false, true, new Treasure(Gem.alexandrite, Gem.aplite));
        Tile tile2 = new Tile(false, true, true, false, new Treasure(Gem.unakite, Gem.hackmanite));

        assertTrue(tile1.connects(UP));
        assertFalse(tile1.connects(DOWN));
        assertFalse(tile1.connects(LEFT));
        assertTrue(tile1.connects(RIGHT));

        assertFalse(tile2.connects(UP));
        assertTrue(tile2.connects(DOWN));
        assertTrue(tile2.connects(LEFT));
        assertFalse(tile2.connects(RIGHT));
    }

    @Test
    public void testRotateTileNegativeTimes() {
        Tile tile1 = new Tile(true, false, false, true, new Treasure(Gem.alexandrite, Gem.aplite));
        assertThrows(IllegalArgumentException.class, () -> tile1.rotate(-4));
    }

    @Test
    public void testRotateTilePositiveTimes() {
        Tile tile1 = new Tile(true, false, false, true, new Treasure(Gem.alexandrite, Gem.aplite));

        tile1.rotate(0);

        assertTrue(tile1.connects(UP));
        assertFalse(tile1.connects(DOWN));
        assertFalse(tile1.connects(LEFT));
        assertTrue(tile1.connects(RIGHT));

        tile1.rotate(1);

        assertFalse(tile1.connects(UP));
        assertTrue(tile1.connects(DOWN));
        assertFalse(tile1.connects(LEFT));
        assertTrue(tile1.connects(RIGHT));

        tile1.rotate(2);

        assertTrue(tile1.connects(UP));
        assertFalse(tile1.connects(DOWN));
        assertTrue(tile1.connects(LEFT));
        assertFalse(tile1.connects(RIGHT));

        tile1.rotate(4);

        assertTrue(tile1.connects(UP));
        assertFalse(tile1.connects(DOWN));
        assertTrue(tile1.connects(LEFT));
        assertFalse(tile1.connects(RIGHT));
    }

    @Test
    public void testTileEquals() {
        Tile tile1 = TestUtils.makeTile(true, true, false, false, TestUtils.makeTreasure(Gem.ammolite, Gem.labradorite));
        Tile tile2 = TestUtils.makeTile(true, true, false, false, TestUtils.makeTreasure(Gem.ammolite, Gem.labradorite));
        assertEquals(tile1, tile2);
    }

    @Test
    public void testCreateInvalidTile() {
        assertThrows(IllegalArgumentException.class, () -> new Tile(Set.of(UP), TestUtils.makeTreasure(Gem.ammolite, Gem.labradorite)));
        assertThrows(IllegalArgumentException.class, () -> new Tile(Set.of(), TestUtils.makeTreasure(Gem.ammolite, Gem.labradorite)));
    }
}
