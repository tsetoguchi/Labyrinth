package referee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import json.JsonSerializer;
import model.Position;
import model.Utils;
import model.board.Board;
import model.board.Direction;
import model.board.IBoard;
import model.board.Tile;
import model.state.IState;
import model.state.PlayerAvatar;
import model.state.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.EuclideanStrategy;
import player.IPlayer;
import player.Player;
import player.RiemannStrategy;

public class GoalHandlerTest {

  GoalHandler goalHandler;
  IState state;
  Referee ref;
  IPlayer player1;
  IPlayer player2;
  List<IPlayer> players;
  PlayerAvatar p1;
  PlayerAvatar p2;
  List<PlayerAvatar> playerAvatars;
  IBoard board;


  @BeforeEach
  public void setup() {
    Tile[][] tiles = new Tile[7][7];

    Set<Direction> cross = new HashSet<>();
    cross.add(Direction.UP);
    cross.add(Direction.DOWN);
    cross.add(Direction.LEFT);
    cross.add(Direction.RIGHT);

    Set<Direction> line = new HashSet<>();
    line.add(Direction.LEFT);
    line.add(Direction.RIGHT);

    for (int i = 0; i < 7; i++) {
      for (int j = 0; j < 7; j++) {
        if (i % 2 == 1) {
          tiles[i][j] = new Tile(cross, null);
        } else {
          tiles[i][j] = new Tile(line, null);
        }
      }
    }


    /*

    BOARD LOOKS LIKE:

    - - - - - - -
    + + + + + + +
    - - - - - - -
    + + + + + + +
    - - - - - - -
    + + + + + + +
    - - - - - - -

     */

    Tile spare = new Tile(line, null);
    this.board = new Board(7, 7, tiles, spare);
    this.p1 = new PlayerAvatar(Color.RED, new Position(3, 1));
    this.p2 = new PlayerAvatar(Color.YELLOW, new Position(3, 5));
    this.playerAvatars = new ArrayList<>();
    this.playerAvatars.addAll(List.of(this.p1, this.p2));
    this.state = new State(this.board, this.playerAvatars);

    this.player1 = new Player("adam", new RiemannStrategy());
    this.player2 = new Player("anakin", new EuclideanStrategy());
    this.players = new ArrayList<>();
    this.players.addAll(List.of(this.player1, this.player2));
    List<Position> goals = Utils.immovablePositionsForBoard(this.board, new DefaultRules());
    this.ref = new Referee(this.state, this.players, goals);
    this.goalHandler = this.ref.goalHandler;
  }

  @Test
  public void runGame() {
    GameResults result = this.ref.runGame();
    assertEquals("[[\"adam\"],[]]", JsonSerializer.gameResults(result).toString());
  }

  @Test
  void nextGoal() {
    this.goalHandler.nextGoal(this.p1);
    assertEquals(1, this.goalHandler.getPlayerGoalCount(this.p1));
  }

  @Test
  void nextGoalNoGoalsLeft() {
    // 9 potential goals given to goal handler
    // 2 goals distributed to players, leaving 7 potential goals
    assertEquals(9, Utils.immovablePositionsForBoard(this.board, new DefaultRules()).size());
    for (int i = 0; i < 100; i++) {
      this.goalHandler.nextGoal(this.p1);
    }
    assertEquals(7, this.goalHandler.getPlayerGoalCount(this.p1));
  }

  @Test
  void playerReachedGoal() {
    assertFalse(this.goalHandler.playerReachedGoal(this.p1));
    this.p1.setCurrentPosition(this.goalHandler.getPlayerCurrentGoal(this.p1));
    assertTrue(this.goalHandler.playerReachedGoal(this.p1));
  }

  @Test
  void getPlayerCurrentGoal() {
    assertEquals(new Position(1, 1), this.goalHandler.getPlayerCurrentGoal(this.p1));
    this.goalHandler.nextGoal(this.p1);
    assertNotEquals(new Position(1, 1), this.goalHandler.getPlayerCurrentGoal(this.p1));
    this.goalHandler.nextGoal(this.p1);
    assertEquals(new Position(3, 1), this.goalHandler.getPlayerCurrentGoal(this.p1));
  }

  @Test
  void anyPlayersHome() {
    this.ref.runGame();
    assertEquals(true, this.goalHandler.anyPlayersHome());
  }

  @Test
  void getPlayerGoalCount() {
    assertEquals(0, this.goalHandler.getPlayerGoalCount(this.p1));
    assertEquals(0, this.goalHandler.getPlayerGoalCount(this.p2));

    for (int i = 0; i < 5; i++) {
      this.goalHandler.nextGoal(this.p1);
    }

    assertEquals(5, this.goalHandler.getPlayerGoalCount(this.p1));
  }


}
