package game.model;

import game.TestUtils;
import game.Exceptions.IllegalGameActionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import static game.model.Direction.*;
import static game.model.Direction.RIGHT;

public class GameTest {
    private Board uniformBoard;
    private Board nonUniformBoard;
    private Game game1;
    private Game game2;
    private PlayerAvatar player1;
    private PlayerAvatar player2;
    private PlayerAvatar player3;
    private List<PlayerAvatar> players;

    @BeforeEach
    public void setup() {
        this.uniformBoard = TestUtils.createUniformBoard(false, false, true, true);
        this.players = new ArrayList<>();
        this.player1 = new PlayerAvatar(Color.blue, new Position(5, 5), new Position(1, 1));
        player1.setCurrentPosition(new Position(0, 0));
        this.player2 = new PlayerAvatar(Color.magenta, new Position(5, 5), new Position(1, 3));
        player2.setCurrentPosition(new Position(0, 1));
        this.player3 = new PlayerAvatar(Color.lightGray, new Position(5, 5), new Position(1, 5));
        player3.setCurrentPosition(new Position(0, 2));
        players.add(player1);
        players.add(player2);
        players.add(player3);
        this.game1 = new Game(this.uniformBoard, this.players);

        this.nonUniformBoard = TestUtils.createNonUniformBoard();
        this.game2 = new Game(this.nonUniformBoard, this.players);
    }

    // --- Constructor Tests ---
    @Test
    public void testThrowsWhenConstructorGetsPlayerWithBadHomeTile() {
        Board board = TestUtils.createUniformBoard(true, true, false, false);
        PlayerAvatar badHomePlayer = new PlayerAvatar(Color.BLUE, new Position(5, 5),
                new Position(0, 0));
        List<PlayerAvatar> players = new ArrayList<>();
        players.add(badHomePlayer);
        Optional<SlideAndInsertRecord> noLastMove = Optional.empty();
        Set<PlayerAvatar> haveSkipped = new HashSet<>();
        assertThrows(IllegalArgumentException.class, () ->
                new Game(board, players, 0, noLastMove, 0, GameStatus.IN_PROGRESS, haveSkipped));
    }

    @Test
    public void testPlayersStartOnHomeTiles() {
        assertEquals(new Position(0, 0), this.player1.getCurrentPosition());
        assertEquals(new Position(0, 1), this.player2.getCurrentPosition());
        assertEquals(new Position(0, 2), this.player3.getCurrentPosition());
    }

    @Test
    public void testGetActivePlayer() {
        assertEquals(player1, this.game1.getActivePlayer());
        assertNotEquals(player2, this.game1.getActivePlayer());
        assertNotEquals(player3, this.game1.getActivePlayer());
    }

    @Test
    public void testGetPlayerList() {
        List<PlayerAvatar> playerList = this.game1.getPlayerList();
        assertEquals(3, playerList.size());
        assertEquals(player1, playerList.get(0));
        assertEquals(player2, playerList.get(1));
        assertEquals(player3, playerList.get(2));
    }

    // --- Slide And Insert Tests ---

    @Test
    public void testInsertRotation() {
        this.game1.slideAndInsert(RIGHT, 0, 0);
        Tile insertedTile = this.game1.getBoard().getTileAt(new Position(0,0));
        assertTrue(insertedTile.connects(LEFT));
        assertTrue(insertedTile.connects(RIGHT));
        assertFalse(insertedTile.connects(UP));
        assertFalse(insertedTile.connects(DOWN));

        this.game1.slideAndInsert(RIGHT, 0, 1);
        Tile insertedTile2 = this.game1.getBoard().getTileAt(new Position(0,0));
        assertFalse(insertedTile2.connects(LEFT));
        assertFalse(insertedTile2.connects(RIGHT));
        assertTrue(insertedTile2.connects(UP));
        assertTrue(insertedTile2.connects(DOWN));
    }

    @Test
    public void testSlideRowRight() {
        this.game2.slideAndInsert(RIGHT, 0, 0);
        Board board = this.game2.getBoard();
        Tile tileAtIndex1 = board.getTileAt(new Position(0, 1));
        Tile tileAtIndex2 = board.getTileAt(new Position(0, 2));
        Tile tileAtIndex3 = board.getTileAt(new Position(0, 3));
        Tile tileAtIndex4 = board.getTileAt(new Position(0, 4));

        assertTrue(tileAtIndex1.connects(UP));
        assertTrue(tileAtIndex2.connects(DOWN));
        assertTrue(tileAtIndex3.connects(LEFT));
        assertTrue(tileAtIndex4.connects(RIGHT));

        assertEquals(new Position(0, 1), this.player1.getCurrentPosition());
        assertEquals(new Position(0, 2), this.player2.getCurrentPosition());
        assertEquals(new Position(0, 3), this.player3.getCurrentPosition());
    }

    @Test
    public void testSlideRowLeft() {
        this.game2.slideAndInsert(LEFT, 0, 0);
        Board board = this.game2.getBoard();
        Tile tileAtIndex0 = board.getTileAt(new Position(0, 0));
        Tile tileAtIndex1 = board.getTileAt(new Position(0, 1));
        Tile tileAtIndex2 = board.getTileAt(new Position(0, 2));
        Tile tileAtIndex6 = board.getTileAt(new Position(0, 6));

        assertTrue(tileAtIndex0.connects(DOWN));
        assertTrue(tileAtIndex1.connects(LEFT));
        assertTrue(tileAtIndex2.connects(RIGHT));

        assertFalse(tileAtIndex6.connects(UP));
        assertFalse(tileAtIndex6.connects(DOWN));
        assertTrue(tileAtIndex6.connects(LEFT));
        assertTrue(tileAtIndex6.connects(RIGHT));

        assertEquals(new Position(0, 6), this.player1.getCurrentPosition());
        assertEquals(new Position(0, 0), this.player2.getCurrentPosition());
        assertEquals(new Position(0, 1), this.player3.getCurrentPosition());
    }

    @Test
    public void testSlideColumnDown() {
        this.game2.slideAndInsert(DOWN, 0, 0);
        Board board = this.game2.getBoard();

        Tile tileAtIndex0 = board.getTileAt(new Position(0, 0));
        Tile tileAtIndex1 = board.getTileAt(new Position(1, 0));

        assertFalse(tileAtIndex0.connects(UP));
        assertFalse(tileAtIndex0.connects(DOWN));
        assertTrue(tileAtIndex0.connects(LEFT));
        assertTrue(tileAtIndex0.connects(RIGHT));

        assertTrue(tileAtIndex1.connects(UP));
        assertFalse(tileAtIndex1.connects(DOWN));
        assertTrue(tileAtIndex1.connects(LEFT));
        assertFalse(tileAtIndex1.connects(RIGHT));

        assertEquals(new Position(1, 0), this.player1.getCurrentPosition());
        assertEquals(new Position(0, 1), this.player2.getCurrentPosition());
        assertEquals(new Position(0, 2), this.player3.getCurrentPosition());
    }

    @Test
    public void testSlideColumnUp() {
        this.game2.slideAndInsert(UP, 0, 0);
        this.game2.slideAndInsert(UP, 0, 0);
        Board board = this.game2.getBoard();

        Tile tileAtIndex0 = board.getTileAt(new Position(0, 0));
        Tile tileAtIndex5 = board.getTileAt(new Position(5, 0));
        Tile tileAtIndex6 = board.getTileAt(new Position(6, 0));

        assertFalse(tileAtIndex0.connects(UP));
        assertFalse(tileAtIndex0.connects(DOWN));
        assertTrue(tileAtIndex0.connects(LEFT));
        assertTrue(tileAtIndex0.connects(RIGHT));

        assertFalse(tileAtIndex5.connects(UP));
        assertFalse(tileAtIndex5.connects(DOWN));
        assertTrue(tileAtIndex5.connects(LEFT));
        assertTrue(tileAtIndex5.connects(RIGHT));

        assertTrue(tileAtIndex6.connects(UP));
        assertFalse(tileAtIndex6.connects(DOWN));
        assertTrue(tileAtIndex6.connects(LEFT));
        assertFalse(tileAtIndex6.connects(RIGHT));

        assertEquals(new Position(5, 0), this.player1.getCurrentPosition());
        assertEquals(new Position(0, 1), this.player2.getCurrentPosition());
        assertEquals(new Position(0, 2), this.player3.getCurrentPosition());
    }

    @Test
    public void testSlideReversesPreviousSlideFails() {
        this.game2.slideAndInsert(UP, 0, 0);
        assertThrows(IllegalGameActionException.class, () -> this.game2.slideAndInsert(DOWN, 0, 0));
    }

    // --- activePlayerCanReachPosition tests ---
    @Test
    public void testActivePlayerCanReachUnreachablePosition() {
        assertFalse(this.game1.activePlayerCanReachPosition(new Position(1, 0)));
        assertFalse(this.game1.activePlayerCanReachPosition(new Position(4, 5)));
        assertFalse(this.game1.activePlayerCanReachPosition(new Position(6, 6)));
        assertFalse(this.game2.activePlayerCanReachPosition(new Position(0, 3)));
        assertFalse(this.game2.activePlayerCanReachPosition(new Position(2, 3)));
    }

    @Test
    public void testActivePlayerCanReachReachablePosition() {
        assertTrue(this.game1.activePlayerCanReachPosition(new Position(0, 1)));
        assertTrue(this.game1.activePlayerCanReachPosition(new Position(0, 3)));
    }

    // --- kickActivePlayer tests ---
    @Test
    public void testKickActivePlayerFromTopOfList() {
        this.game1.kickActivePlayer();
        assertEquals(player2, this.game1.getActivePlayer());
        assertEquals(2, this.game1.getPlayerList().size());
        this.game1.kickActivePlayer();
        assertEquals(player3, this.game1.getActivePlayer());
        assertEquals(1, this.game1.getPlayerList().size());
    }

    @Test
    public void testKickActivePlayerFromBottomOfList() {
        this.game1.skipTurn();
        this.game1.skipTurn();
        this.game1.kickActivePlayer();
        assertEquals(player1, this.game1.getActivePlayer());
        assertEquals(2, this.game1.getPlayerList().size());
    }
    @Test
    public void testSkippingAndKickingEndingGame() {
        this.game1.skipTurn();
        this.game1.skipTurn();
        this.game1.kickActivePlayer();
        assertEquals(GameStatus.ALL_SKIPPED, this.game1.getGameStatus());
    }

    // --- skipTurn tests ---
    @Test
    public void testSkipTurnWithoutEnding() {
        assertEquals(this.player1, this.game1.getActivePlayer());
        this.game1.skipTurn();
        assertEquals(this.player2, this.game1.getActivePlayer());
        this.game1.skipTurn();
        assertEquals(this.player3, this.game1.getActivePlayer());
    }
    @Test
    public void testSkippingConsecutivelyEndingGame() {
        this.game1.skipTurn();
        this.game1.skipTurn();
        this.game1.skipTurn();
        assertEquals(GameStatus.ALL_SKIPPED, this.game1.getGameStatus());
    }

    // --- moveActivePlayer tests ---

    @Test
    public void testMovingToInaccessibleLocationThrowsError() {
        this.game1.slideAndInsert(DOWN, 6, 0);
        assertThrows(IllegalGameActionException.class, () -> this.game1.moveActivePlayer(new Position(2, 5)));
    }

    @Test
    public void testMovingToLocationBecomesInaccessibleDueToRotation() {
        this.game1.slideAndInsert(DOWN, 6, 1);
        assertThrows(IllegalGameActionException.class, () -> this.game1.moveActivePlayer(new Position(0, 6)));
    }

    @Test
    public void testFullRoundOfTurns() {
        assertEquals(this.player1, this.game1.getActivePlayer()); // 0, 0 -> 6, 0
        this.game1.slideAndInsert(UP, 0, 0);
        this.game1.moveActivePlayer(new Position(6, 5));
        assertEquals(this.player2, this.game1.getActivePlayer()); // 0, 1
        this.game1.slideAndInsert(UP, 0, 0);
        this.game1.moveActivePlayer(new Position(0, 3));
        assertEquals(this.player3, this.game1.getActivePlayer()); // 0, 2
        this.game1.slideAndInsert(UP, 0, 0);
        this.game1.moveActivePlayer(new Position(0, 4));
        assertEquals(this.player1, this.game1.getActivePlayer());
    }

    @Test
    public void testPlayerHasReachedGoal() {
        Board testBoard = TestUtils.createUniformBoard(true, true, true, true);
        List<PlayerAvatar> testPlayers = new ArrayList<>();
        PlayerAvatar testPlayer = new PlayerAvatar(Color.green, new Position(1, 5),
                new Position(1, 1));
        testPlayer.setCurrentPosition(new Position(0, 1));
        testPlayers.add(testPlayer);
        Game testGame = new Game(testBoard, testPlayers);

        assertFalse(testGame.getActivePlayer().hasReachedGoal());
        testGame.slideAndInsert(UP, 0, 0);
        testGame.moveActivePlayer(new Position(1, 5));
        assertTrue(testGame.getActivePlayer().hasReachedGoal());
    }

    @Test
    public void testPlayerHasNotReachedGoal() {
        Board testBoard = TestUtils.createUniformBoard(true, true, true, true);
        List<PlayerAvatar> testPlayers = new ArrayList<>();
        PlayerAvatar testPlayer1 = new PlayerAvatar(Color.red, new Position(3, 3),
                new Position(3, 3));
        PlayerAvatar testPlayer2 = new PlayerAvatar(Color.black, new Position(5, 3),
                new Position(3, 5));
        PlayerAvatar testPlayer3 = new PlayerAvatar(Color.white, new Position(5, 5),
                new Position(5, 7));

        testPlayers.add(testPlayer1);
        testPlayers.add(testPlayer2);
        testPlayers.add(testPlayer3);
        Game testGame = new Game(testBoard, testPlayers);

        assertFalse(testGame.getActivePlayer().hasReachedGoal());
        testGame.skipTurn();
        assertFalse(testGame.getActivePlayer().hasReachedGoal());
        testGame.skipTurn();
        assertFalse(testGame.getActivePlayer().hasReachedGoal());
    }

    @Test
    public void testKickingAllPlayersEndingGame() {
        this.game1.kickActivePlayer();
        this.game1.kickActivePlayer();
        this.game1.kickActivePlayer();
        assertEquals(GameStatus.NO_REMAINING_PLAYERS, this.game1.getGameStatus());
    }
}
