package game.model;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TreasureTest {
    @Test
    public void testCreateTreasureWithExactlyTwoGems() {
        assertDoesNotThrow(() -> new Treasure(Gem.alexandrite, Gem.blue_ceylon_sapphire));
    }

    @Test
    public void testCreateTreasureWithoutExactlyTwoGems() {
        List<Gem> tooManyGems = new ArrayList<>();
        List<Gem> notEnoughGems = new ArrayList<>();
        tooManyGems.add(Gem.aventurine);
        tooManyGems.add(Gem.aventurine);
        tooManyGems.add(Gem.aventurine);
        notEnoughGems.add(Gem.spinel);
        assertThrows(IllegalArgumentException.class, () -> new Treasure(tooManyGems));
        assertThrows(IllegalArgumentException.class, () -> new Treasure(notEnoughGems));
        assertThrows(IllegalArgumentException.class, () -> new Treasure(new ArrayList<>()));
    }

    @Test
    public void testTreasuresWithSameGemsAreEqual() {
        Treasure treasure = new Treasure(Gem.alexandrite, Gem.blue_ceylon_sapphire);
        Treasure sameTreasure = new Treasure(Gem.alexandrite, Gem.blue_ceylon_sapphire);
        Treasure differentTreasure1 = new Treasure(Gem.alexandrite, Gem.aplite);
        Treasure differentTreasure2 = new Treasure(Gem.unakite, Gem.hackmanite);

        assertEquals(treasure, sameTreasure);
        assertNotEquals(treasure, differentTreasure1);
        assertNotEquals(treasure, differentTreasure2);
    }
}
