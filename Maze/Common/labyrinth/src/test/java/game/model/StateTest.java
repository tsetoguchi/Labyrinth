package model.model;

import model.TestUtils;
import model.Exceptions.IllegalGameActionException;
import model.board.IBoard;
import model.board.Tile;
import model.state.GameStatus;
import model.state.PlayerAvatar;
import model.state.SlideAndInsertRecord;
import model.state.State;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import static model.board.Direction.*;
import static model.board.Direction.RIGHT;

public class StateTest {
    private IBoard uniformBoard;
    private IBoard nonUniformBoard;
    private State state1;
    private State state2;
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
        this.state1 = new State(this.uniformBoard, this.players);

        this.nonUniformBoard = TestUtils.createNonUniformBoard();
        this.state2 = new State(this.nonUniformBoard, this.players);
    }

    // --- Constructor Tests ---
    @Test
    public void testThrowsWhenConstructorGetsPlayerWithBadHomeTile() {
        IBoard board = TestUtils.createUniformBoard(true, true, false, false);
        PlayerAvatar badHomePlayer = new PlayerAvatar(Color.BLUE, new Position(5, 5),
                new Position(0, 0));
        List<PlayerAvatar> players = new ArrayList<>();
        players.add(badHomePlayer);
        Optional<SlideAndInsertRecord> noLastMove = Optional.empty();
        Set<PlayerAvatar> haveSkipped = new HashSet<>();
        assertThrows(IllegalArgumentException.class, () ->
                new State(board, players, 0, noLastMove, 0, GameStatus.IN_PROGRESS, haveSkipped));
    }

    @Test
    public void testPlayersStartOnHomeTiles() {
        assertEquals(new Position(0, 0), this.player1.getCurrentPosition());
        assertEquals(new Position(0, 1), this.player2.getCurrentPosition());
        assertEquals(new Position(0, 2), this.player3.getCurrentPosition());
    }

    @Test
    public void testGetActivePlayer() {
        assertEquals(player1, this.state1.getActivePlayer());
        assertNotEquals(player2, this.state1.getActivePlayer());
        assertNotEquals(player3, this.state1.getActivePlayer());
    }

    @Test
    public void testGetPlayerList() {
        List<PlayerAvatar> playerList = this.state1.getPlayerList();
        assertEquals(3, playerList.size());
        assertEquals(player1, playerList.get(0));
        assertEquals(player2, playerList.get(1));
        assertEquals(player3, playerList.get(2));
    }

    // --- Slide And Insert Tests ---

    @Test
    public void testInsertRotation() {
        this.state1.slideAndInsert(RIGHT, 0, 0);
        Tile insertedTile = this.state1.getBoard().getTileAt(new Position(0,0));
        assertTrue(insertedTile.connects(LEFT));
        assertTrue(insertedTile.connects(RIGHT));
        assertFalse(insertedTile.connects(UP));
        assertFalse(insertedTile.connects(DOWN));

        this.state1.slideAndInsert(RIGHT, 0, 1);
        Tile insertedTile2 = this.state1.getBoard().getTileAt(new Position(0,0));
        assertFalse(insertedTile2.connects(LEFT));
        assertFalse(insertedTile2.connects(RIGHT));
        assertTrue(insertedTile2.connects(UP));
        assertTrue(insertedTile2.connects(DOWN));
    }

    @Test
    public void testSlideRowRight() {
        this.state2.slideAndInsert(RIGHT, 0, 0);
        IBoard board = this.state2.getBoard();
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
        this.state2.slideAndInsert(LEFT, 0, 0);
        IBoard board = this.state2.getBoard();
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
        this.state2.slideAndInsert(DOWN, 0, 0);
        IBoard board = this.state2.getBoard();

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
        this.state2.slideAndInsert(UP, 0, 0);
        this.state2.slideAndInsert(UP, 0, 0);
        IBoard board = this.state2.getBoard();

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
        this.state2.slideAndInsert(UP, 0, 0);
        assertThrows(IllegalGameActionException.class, () -> this.state2.slideAndInsert(DOWN, 0, 0));
    }

    // --- activePlayerCanReachPosition tests ---
    @Test
    public void testActivePlayerCanReachUnreachablePosition() {
        assertFalse(this.state1.activePlayerCanReachPosition(new Position(1, 0)));
        assertFalse(this.state1.activePlayerCanReachPosition(new Position(4, 5)));
        assertFalse(this.state1.activePlayerCanReachPosition(new Position(6, 6)));
        assertFalse(this.state2.activePlayerCanReachPosition(new Position(0, 3)));
        assertFalse(this.state2.activePlayerCanReachPosition(new Position(2, 3)));
    }

    @Test
    public void testActivePlayerCanReachReachablePosition() {
        assertTrue(this.state1.activePlayerCanReachPosition(new Position(0, 1)));
        assertTrue(this.state1.activePlayerCanReachPosition(new Position(0, 3)));
    }

    // --- kickActivePlayer tests ---
    @Test
    public void testKickActivePlayerFromTopOfList() {
        this.state1.kickActivePlayer();
        assertEquals(player2, this.state1.getActivePlayer());
        assertEquals(2, this.state1.getPlayerList().size());
        this.state1.kickActivePlayer();
        assertEquals(player3, this.state1.getActivePlayer());
        assertEquals(1, this.state1.getPlayerList().size());
    }

    @Test
    public void testKickActivePlayerFromBottomOfList() {
        this.state1.skipTurn();
        this.state1.skipTurn();
        this.state1.kickActivePlayer();
        assertEquals(player1, this.state1.getActivePlayer());
        assertEquals(2, this.state1.getPlayerList().size());
    }
    @Test
    public void testSkippingAndKickingEndingGame() {
        this.state1.skipTurn();
        this.state1.skipTurn();
        this.state1.kickActivePlayer();
        assertEquals(GameStatus.ALL_SKIPPED, this.state1.getGameStatus());
    }

    // --- skipTurn tests ---
    @Test
    public void testSkipTurnWithoutEnding() {
        assertEquals(this.player1, this.state1.getActivePlayer());
        this.state1.skipTurn();
        assertEquals(this.player2, this.state1.getActivePlayer());
        this.state1.skipTurn();
        assertEquals(this.player3, this.state1.getActivePlayer());
    }
    @Test
    public void testSkippingConsecutivelyEndingGame() {
        this.state1.skipTurn();
        this.state1.skipTurn();
        this.state1.skipTurn();
        assertEquals(GameStatus.ALL_SKIPPED, this.state1.getGameStatus());
    }

    // --- moveActivePlayer tests ---

    @Test
    public void testMovingToInaccessibleLocationThrowsError() {
        this.state1.slideAndInsert(DOWN, 6, 0);
        assertThrows(IllegalGameActionException.class, () -> this.state1.moveActivePlayer(new Position(2, 5)));
    }

    @Test
    public void testMovingToLocationBecomesInaccessibleDueToRotation() {
        this.state1.slideAndInsert(DOWN, 6, 1);
        assertThrows(IllegalGameActionException.class, () -> this.state1.moveActivePlayer(new Position(0, 6)));
    }

    @Test
    public void testFullRoundOfTurns() {
        assertEquals(this.player1, this.state1.getActivePlayer()); // 0, 0 -> 6, 0
        this.state1.slideAndInsert(UP, 0, 0);
        this.state1.moveActivePlayer(new Position(6, 5));
        assertEquals(this.player2, this.state1.getActivePlayer()); // 0, 1
        this.state1.slideAndInsert(UP, 0, 0);
        this.state1.moveActivePlayer(new Position(0, 3));
        assertEquals(this.player3, this.state1.getActivePlayer()); // 0, 2
        this.state1.slideAndInsert(UP, 0, 0);
        this.state1.moveActivePlayer(new Position(0, 4));
        assertEquals(this.player1, this.state1.getActivePlayer());
    }

    @Test
    public void testPlayerHasReachedGoal() {
        IBoard testBoard = TestUtils.createUniformBoard(true, true, true, true);
        List<PlayerAvatar> testPlayers = new ArrayList<>();
        PlayerAvatar testPlayer = new PlayerAvatar(Color.green, new Position(1, 5),
                new Position(1, 1));
        testPlayer.setCurrentPosition(new Position(0, 1));
        testPlayers.add(testPlayer);
        State testState = new State(testBoard, testPlayers);

        assertFalse(testState.getActivePlayer().hasReachedGoal());
        testState.slideAndInsert(UP, 0, 0);
        testState.moveActivePlayer(new Position(1, 5));
        assertTrue(testState.getActivePlayer().hasReachedGoal());
    }

    @Test
    public void testPlayerHasNotReachedGoal() {
        IBoard testBoard = TestUtils.createUniformBoard(true, true, true, true);
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
        State testState = new State(testBoard, testPlayers);

        assertFalse(testState.getActivePlayer().hasReachedGoal());
        testState.skipTurn();
        assertFalse(testState.getActivePlayer().hasReachedGoal());
        testState.skipTurn();
        assertFalse(testState.getActivePlayer().hasReachedGoal());
    }

    @Test
    public void testKickingAllPlayersEndingGame() {
        this.state1.kickActivePlayer();
        this.state1.kickActivePlayer();
        this.state1.kickActivePlayer();
        assertEquals(GameStatus.NO_REMAINING_PLAYERS, this.state1.getGameStatus());
    }
}
