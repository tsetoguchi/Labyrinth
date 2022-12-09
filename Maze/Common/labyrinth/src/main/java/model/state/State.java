package model.state;

import static model.board.Direction.*;
import static model.state.GameStatus.*;

import model.Exceptions.IllegalGameActionException;
import model.board.Direction;
import model.Position;
import model.board.ExperimentationBoard;
import model.board.IBoard;
import referee.Move;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Represents the game state.
 */
public class State implements IState {

  private static final int MAX_ROUNDS = 1000;

  private final IBoard board;

  /**
   * Players are stored in this List in terms of turn order.
   **/
  private final List<PlayerAvatar> playerList;
  /**
   * An index into playerList.
   **/
  private int activePlayer;
  /**
   * A record of the slide and insert action that was performed last turn. Empty on the first turn.
   **/
  private Optional<SlideAndInsertRecord> previousSlideAndInsert;
  /**
   * The number of rounds that have elapsed in the game, with a new round starting each time the 0th
   * player becomes active.
   **/
  private int roundsElapsed;
  /**
   * Either IN_PROGRESS while the game is being played, or the appropriate game over state.
   **/
  private GameStatus status;
  /**
   * A set of all players who skipped during their last turn
   **/
  private Set<PlayerAvatar> haveSkipped;

  /*
  Constructors:
   */

  /**
   * Creates a new State representing a game in progress with the given properties. The turn order
   * is defined by the Player list order. Throws an exception if a game is initialized with an
   * illegal IBoard or set of Players.
   */
  public State(IBoard board, List<PlayerAvatar> playerList, int activePlayer,
      Optional<SlideAndInsertRecord> previousSlideAndInsert, int roundsElapsed,
      GameStatus status, Set<PlayerAvatar> haveSkipped) {
    this.board = board;
    this.playerList = new ArrayList<>();
    if (playerList == null) {
      throw new IllegalArgumentException("State received a null playerList.");
    }
    this.playerList.addAll(playerList);
    this.activePlayer = activePlayer;
    this.previousSlideAndInsert = previousSlideAndInsert;
    this.roundsElapsed = roundsElapsed;
    this.status = status;
    this.haveSkipped = haveSkipped;

    this.validateGameConstruction();

    if (this.playerList.size() == 0) {
      this.status = NO_REMAINING_PLAYERS;
    }
  }

  /**
   * Creates a new State with the given list of Players and board.
   */
  public State(IBoard board, List<PlayerAvatar> playerList) {
    this(board, playerList, 0, Optional.empty(), 0, IN_PROGRESS, new HashSet<>());
  }

  /**
   * Creates a new game with the given board, players, optional previous slide and insert, and
   * active player.
   */
  public State(IBoard board, List<PlayerAvatar> playerList,
      SlideAndInsertRecord previousSlideAndInsert) {
    this(board, playerList, 0, Optional.of(previousSlideAndInsert),
        0, IN_PROGRESS, new HashSet<>());
  }

  /**
   * Verifies that all fields are non-null and enforces all game constraints
   */
  private void validateGameConstruction() {

    Set<Color> existingPlayerColors = new HashSet<>();
    Set<Position> existingHomePositions = new HashSet<>();
    for (PlayerAvatar player : this.playerList) {
      Position homePosition = player.getHome();

      if (homePosition.getRow() % 2 == 0 || homePosition.getColumn() % 2 == 0) {
        throw new IllegalArgumentException("Home tile is not on an immovable Tile.");
      }

      if (existingHomePositions.contains(homePosition)) {
//        throw new IllegalArgumentException("Home tiles must be distinct.");
      } else {
        existingHomePositions.add(homePosition);
      }

//      if (existingPlayerColors.contains(player.getColor())) {
//        throw new IllegalArgumentException("Duplicate player colors are not allowed.");
//      } else {
      existingPlayerColors.add(player.getColor());
//      }

      if (this.activePlayer < 0 || this.activePlayer >= this.playerList.size()) {
        throw new IllegalArgumentException("Invalid active player index given.");
      }

      if (this.roundsElapsed < 0) {
        throw new IllegalArgumentException("Rounds elapsed can't be negative.");
      }
    }

    if (this.board == null || this.previousSlideAndInsert == null || this.status == null
        || this.haveSkipped == null) {
      throw new IllegalArgumentException("Fields cannot be null.");
    }
  }

  /*
  Turn Handling
   */

  /**
   * Moves the game to the next turn by updating the active Player. Advances the round counter if
   * the top of the turn order is reached.
   */
  private void nextTurn() {
    this.activePlayer = (this.activePlayer + 1) % this.playerList.size();

    // handle round logic at the start of every new round
    if (this.activePlayer == 0) {
      this.roundHandler();
    }
  }

  /**
   * Increments the round count and checks if the state has run more than the MAX_ROUNDS or if all
   * players have passed.
   */
  private void roundHandler() {
    this.roundsElapsed += 1;

    if (this.roundsElapsed > MAX_ROUNDS) {
      this.status = ROUND_LIMIT_REACHED;
    }

    if (this.haveSkipped.containsAll(this.playerList)) {
      this.status = ALL_SKIPPED;
    }
  }

  /**
   * Kicks the player with the specified color from the game
   */
  public void kickPlayer(PlayerAvatar player) {

    int playerIndex = this.playerList.indexOf(player);

    if (playerIndex < this.activePlayer && playerIndex != -1) {
      this.activePlayer--;
    }

    this.playerList.remove(player);
    this.haveSkipped.remove(player);

    if (this.playerList.size() == 0) {
      this.status = NO_REMAINING_PLAYERS;
    } else {
      this.activePlayer = this.activePlayer % this.playerList.size();
    }


  }

  /**
   * Skips the current player's turn.
   */
  public void skipTurn() {
    this.haveSkipped.add(this.getActivePlayer());
    this.nextTurn();
  }

  /**
   * Returns true if the game is over, else returns false.
   */
  public boolean isGameOver() {
    return this.status != IN_PROGRESS;
  }

  /*
  Move Execution:
   */

  /**
   * Executes a full turn in the state. First the move is validated, then applied. Then the active
   * player currently making the move is repositioned to the destination of the move. Lastly, we set
   * the state to the next turn.
   */
  public void executeTurn(Move move) {
    this.assertValidTurn(move);
    this.slideAndInsert(move);
    this.getActivePlayer().setCurrentPosition(move.getMoveDestination());
    this.nextTurn();
  }

  /**
   * Validates the given move by seeing if the destination of the move is reachable.
   */
  private void assertValidTurn(Move move) throws IllegalGameActionException {
    Position current = this.getActivePlayer().getCurrentPosition();
    ExperimentationBoard eBoard = this.board.getExperimentationBoard();
    Set<Position> reachable = eBoard.findReachableTilePositionsAfterSlideAndInsert(move, current);
    if (!reachable.contains(move.getMoveDestination())) {
      throw new IllegalGameActionException("Illegal move: " + move);
    }
  }

  /**
   * Apply the given move to this state.
   */
  private void slideAndInsert(Move move) {
    int index = move.getSlideIndex();
    int rotations = move.getSpareTileRotations();
    Direction direction = move.getSlideDirection();
    if (this.doesSlideUndoPrevious(direction, index)) {
      throw new IllegalGameActionException(
          "Attempted to perform a slide which undoes the previous slide.");
    }
    this.board.slideAndInsert(direction, index, rotations);

    this.slidePlayers(direction, index);
    this.previousSlideAndInsert = Optional.of(
        new SlideAndInsertRecord(direction, index, rotations));

    this.haveSkipped = new HashSet<>();
  }

  /**
   * Update the positions of each of the players who were on a tile that was moved during a Slide.
   */
  private void slidePlayers(Direction direction, int index) {
    int boardHeight = this.board.getHeight();
    int boardWidth = this.board.getWidth();

    for (PlayerAvatar player : this.playerList) {

      Position playerPosition = player.getCurrentPosition();

      if (direction == UP && playerPosition.getColumn() == index) {
        player.setCurrentPosition(
            playerPosition.addDeltaWithBoardWrap(-1, 0, boardHeight, boardWidth));
      } else if (direction == DOWN && playerPosition.getColumn() == index) {
        player.setCurrentPosition(
            playerPosition.addDeltaWithBoardWrap(1, 0, boardHeight, boardWidth));
      } else if (direction == LEFT && playerPosition.getRow() == index) {
        player.setCurrentPosition(
            playerPosition.addDeltaWithBoardWrap(0, -1, boardHeight, boardWidth));
      } else if (direction == RIGHT && playerPosition.getRow() == index) {
        player.setCurrentPosition(
            playerPosition.addDeltaWithBoardWrap(0, 1, boardHeight, boardWidth));
      }
    }
  }

  /**
   * Returns true if the given slide and insert is the opposite of the previous side and insert.
   * Otherwise, returns false.
   */
  private boolean doesSlideUndoPrevious(Direction direction, int index) {
    if (this.previousSlideAndInsert.isPresent()) {
      return this.previousSlideAndInsert.get().getDirection() == Direction.opposite(direction)
          && this.previousSlideAndInsert.get().getIndex() == index;
    }
    return false;
  }

  /*
  Getters:
   */

  public PlayerAvatar getActivePlayer() {
    return this.playerList.get(this.activePlayer);
  }

  public int getBoardWidth() {
    return this.board.getWidth();
  }

  public int getBoardHeight() {
    return this.board.getHeight();
  }

  public IBoard getBoard() {
    return this.board;
  }

  public List<PlayerAvatar> getPlayerList() {
    List<PlayerAvatar> players = new ArrayList<>();
    int size = this.playerList.size();
    for (int i = 0; i < size; i++) {
      PlayerAvatar p = this.playerList.get((this.activePlayer + i) % size);
      players.add(p.deepCopy());
      //players.add(p);
    }
    return players;
  }

  public GameStatus getGameStatus() {
    return this.status;
  }

  public Optional<SlideAndInsertRecord> getPreviousSlideAndInsert() {
    return this.previousSlideAndInsert;
  }

  public StateProjection getStateProjection() {
    return new StateProjection(this.getBoard().getExperimentationBoard(), this.getPlayerList(),
        this.previousSlideAndInsert);
  }

}
