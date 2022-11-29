package game.model;

import static game.model.Direction.*;
import static game.model.GameStatus.*;

import game.Exceptions.IllegalGameActionException;
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

  /**
   * Creates a new State representing a game in progress with the given properties. The turn order is
   * defined by the Player list order. Throws an exception if a game is initialized with an illegal
   * IBoard or set of Players.
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
  public State(IBoard board, List<PlayerAvatar> playerList, int activePlayer,
               Optional<SlideAndInsertRecord> previousSlideAndInsert) {
    this(board, playerList, activePlayer, previousSlideAndInsert, 0, IN_PROGRESS, new HashSet<>());
  }

  /**
   * Slides the row (for left or right slides) or column (for up or down slides) at the given index
   * in the specified direction, then inserts the spare tile after rotating it the given number of
   * times. If a Player is moved off the board, move the Player to the newly inserted tile.
   *
   * @precondition isValidSlideAndInsert has been called and returned true
   */
  public void slideAndInsert(Direction direction, int index, int rotations) {
    this.assertGameIsNotOver();
    if (doesSlideUndoPrevious(direction, index)) {
      throw new IllegalGameActionException(
          "Attempted to perform a slide which undoes the previous slide.");
    }
    this.board.slideAndInsert(direction, index, rotations);

    this.slidePlayers(direction, index);
    this.previousSlideAndInsert = Optional.of(
        new SlideAndInsertRecord(direction, index, rotations));

    this.haveSkipped = new HashSet<>();
  }

  public boolean activePlayerCanReachPosition(Position positionToReach) {
    Set<Position> reachableTiles = this.board.getReachablePositions(
        this.getActivePlayer().getCurrentPosition());

    return reachableTiles.contains(positionToReach);
  }


  /**
   * Kicks the player with the specified color from the game
   */
  public void kickPlayer(PlayerAvatar player) {
    if (this.playerList.size() == 0) {
      throw new IllegalGameActionException("Tried to kick the active player when there were no" +
          "players in the game.");
    }

    this.playerList.remove(player);
    this.haveSkipped.remove(player);

    if (this.playerList.size() == 0) {
      this.status = NO_REMAINING_PLAYERS;
    }
  }

  /**
   * Skips the current player's turn.
   */
  public void skipTurn() {
    this.assertGameIsNotOver();
    this.haveSkipped.add(this.playerList.get(this.activePlayer));
    if (this.haveSkipped.containsAll(this.playerList)) {
      this.status = ALL_SKIPPED;
    }
    this.nextTurn();
  }

  /**
   * Moves the active player to the given destination, if it can be reached, and throws an exception
   * if not.
   */
  public void moveActivePlayer(Position destination) {
    this.assertGameIsNotOver();
    if (!this.activePlayerCanReachPosition(destination)) {
      throw new IllegalGameActionException(
          "Tried to move the active player to an unreachable tile.");
    }

    this.getActivePlayer().setCurrentPosition(destination);

    if (this.getActivePlayer().hasReachedGoal() && this.activePlayerIsOnHomeTile()) {
      this.status = TREASURE_RETURNED;
    }
    if (this.activePlayerIsOnGoalTile()) {
      this.getActivePlayer().setHasReachedGoal(true);
    }
    this.nextTurn();
  }

//    public boolean isValidSlideAndInsert(Direction direction, int index, int rotations) {
//        return !this.doesSlideUndoPrevious(direction, index) &&
//                this.board.getRules().isValidSlideAndInsert(direction, index, rotations);
//    }

  public PlayerAvatar getActivePlayer() {
    return this.playerList.get(this.activePlayer);
  }

  public IBoard getBoard() {
    return this.board;
  }

  public List<PlayerAvatar> getPlayerList() {
    return this.playerList;
  }

  public GameStatus getGameStatus() {
    return this.status;
  }

  public Optional<SlideAndInsertRecord> getPreviousSlideAndInsert() {
    return this.previousSlideAndInsert;
  }

  public Optional<PlayerAvatar> getPlayer(Color color) {
    for (PlayerAvatar player : this.playerList) {
      if (player.getColor().equals(color)) {
        return Optional.of(player);
      }
    }
    return Optional.empty();
  }

  /**
   * Verifies that all fields are non-null and enforces all game constraints
   */
  private void validateGameConstruction() {

    Set<Color> existingPlayerColors = new HashSet<>();
    Set<Position> existingHomePositions = new HashSet<>();
    for (PlayerAvatar player : this.playerList) {
      Position goalPosition = player.getGoal();
      Position homePosition = player.getHome();

      if (goalPosition.getRow() % 2 == 0 || goalPosition.getColumn() % 2 == 0) {
        throw new IllegalArgumentException(
            String.format("Goal tile (%d, %d) is not on an immovable Tile.",
                goalPosition.getRow(), goalPosition.getColumn()));
      }
      if (homePosition.getRow() % 2 == 0 || homePosition.getColumn() % 2 == 0) {
        throw new IllegalArgumentException("Home tile is not on an immovable Tile.");
      }

      if (existingHomePositions.contains(homePosition)) {
//        throw new IllegalArgumentException("Home tiles must be distinct.");
      } else {
        existingHomePositions.add(homePosition);
      }

      if (existingPlayerColors.contains(player.getColor())) {
        throw new IllegalArgumentException("Duplicate player colors are not allowed.");
      } else {
        existingPlayerColors.add(player.getColor());
      }

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

  private boolean activePlayerIsOnGoalTile() {
    PlayerAvatar currentPlayer = this.getActivePlayer();
    return currentPlayer.getCurrentPosition().equals(currentPlayer.getGoal());
  }

  private void assertGameIsNotOver() {
    if (this.status != IN_PROGRESS) {
      throw new IllegalGameActionException(
          "Tried to perform a game action after the game was over.");
    }
  }

  private boolean activePlayerIsOnHomeTile() {
    Position playerHome = this.getActivePlayer().getHome();
    return this.getActivePlayer().getCurrentPosition().equals(playerHome);
  }

  /**
   * Moves the game to the next turn by updating the active Player. Advances the round counter if
   * the top of the turn order is reached.
   */
  private void nextTurn() {
    if (this.activePlayer < 0 || this.activePlayer >= this.playerList.size()) {
      throw new IllegalStateException(String.format("The active Player index %d is " +
          "outside the bounds of the player List.", this.activePlayer));
    }
    if (this.activePlayer == this.playerList.size() - 1) {
      this.activePlayer = 0;
    } else {
      this.activePlayer++;
    }
    if (this.activePlayer == 0) {
      this.roundsElapsed += 1;
      if (this.roundsElapsed > 1000) {
        this.status = ROUND_LIMIT_REACHED;
      }
    }
  }

  private boolean doesSlideUndoPrevious(Direction direction, int index) {
    if (this.previousSlideAndInsert.isPresent()) {
      return this.previousSlideAndInsert.get().getDirection() == Direction.opposite(direction)
          && this.previousSlideAndInsert.get().getIndex() == index;
    }
    return false;
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
}
