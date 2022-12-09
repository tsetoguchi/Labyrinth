package referee;

import model.Position;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import model.board.ExperimentationBoard;
import model.board.IBoard;
import model.state.StateProjection;
import model.state.IState;
import model.state.PlayerAvatar;
import observer.Controller.IObserver;
import player.IPlayer;

import java.util.*;

/**
 * Manages a game by managing the turns of the players and running it to completion. Kicks any
 * players that misbehave, fail to respond, or throw an error, and returns the result of the game.
 */
public class Referee implements IReferee {

  private static final int TIMEOUT = 4;

  private final IState game;
  private final IRules rules;
  protected final GoalHandler goalHandler;

  private final Map<PlayerAvatar, PlayerHandler> playerAvatarToHandler;
  private final List<PlayerAvatar> eliminated;
  private final List<PlayerAvatar> winners;

  private final List<IObserver> observers;

  /**
   * Creates a referee and assigns it a game to moderate, as well as a list of clients which should
   * be used to interface with players. The order of the clients in the list must correspond to the
   * order of players in the State. Accepts an optional observer which will be updated as the game
   * progresses.
   */
  public Referee(IState game, List<IPlayer> players,
      List<IObserver> observers, IRules rules, List<Position> goals) {
    this.game = game;
    this.rules = rules;
    this.playerAvatarToHandler = this.mapPlayerAvatarsToPlayerHandlers(game, players);
    this.observers = observers;
    this.eliminated = new ArrayList<>();
    this.winners = new ArrayList<>();
    this.goalHandler = new GoalHandler(game.getPlayerList(), goals);
  }

  public Referee(IState game, List<IPlayer> players, List<Position> goals) {
    this(game, players, List.of(), new DefaultRules(), goals);
  }

  /**
   * Initialize a Referee with a just a set of proxy players, building a new randomized State from
   * scratch.
   */
//  public Referee(List<IPlayer> players, IRules rules) {
//    List<IBoard> proposedBoards = new ArrayList<>();
//    List<Color> uniqueColors = this.generateUniqueColors(players.size());
//
//    // get proposed board
//    List<PlayerHandler> playerHandlers = this.interfaceToHandlers(players, uniqueColors);
//    List<PlayerHandler> invalidProposals = new ArrayList<>();
//    for (PlayerHandler playerHandler : playerHandlers) {
//      Optional<IBoard> board = playerHandler
//          .proposeBoard(this.game.getBoard().getHeight(), this.game.getBoard().getWidth());
//      if (board.isPresent()) {
//        proposedBoards.add(board.get());
//      } else {
//        // proposal was invalid
//        invalidProposals.add(playerHandler);
//      }
//    }
//    for (PlayerHandler playerHandler : invalidProposals) {
//      playerHandlers.remove(playerHandler);
//    }
//    IBoard board = proposedBoards.get(new Random().nextInt(proposedBoards.size() - 1));
//
//    List<Position> immovablePositions = this.immovablePositionsForBoard(board);
//    Collections.shuffle(immovablePositions);
//
//    List<PlayerAvatar> playerAvatars = new ArrayList<>();
//    List<Position> homes = new ArrayList<>(immovablePositions);
//    Collections.shuffle(immovablePositions);
//
//    List<Position> goals = new ArrayList<>(immovablePositions);
//
//    for (int i = 0; i < uniqueColors.size(); i++) {
//      playerAvatars.add(new PlayerAvatar(uniqueColors.get(i), goals.get(i), homes.get(i)));
//    }
//    IState game = new State(board, playerAvatars);
//    this.game = game;
//    this.rules = rules;
//    this.playerAvatarToHandler = this.mapPlayerAvatarsToPlayerHandlers(game, players);
//    this.playersCollectedTreasures = new ArrayList<>();
//    this.observers = new ArrayList<>();
//    this.eliminated = new ArrayList<>();
//  }

  /**
   * Builds a map of the player avatars to their corresponding handlers.
   */
  protected Map<PlayerAvatar, PlayerHandler> mapPlayerAvatarsToPlayerHandlers(IState game,
      List<IPlayer> IPlayers) {
    if (IPlayers.size() != game.getPlayerList().size()) {
      throw new IllegalArgumentException("Amount of clients and players do not match.");
    }

    Map<PlayerAvatar, PlayerHandler> playersToHandlers = new HashMap<>();

    for (int i = 0; i < game.getPlayerList().size(); i++) {
      PlayerAvatar playerAvatar = game.getPlayerList().get(i);
      PlayerHandler playerHandler = new PlayerHandler(IPlayers.get(i));
      playersToHandlers.put(playerAvatar, playerHandler);
    }
    return playersToHandlers;
  }

  /*
  Game Running
   */

  /**
   * Runs the game this referee is moderating to completion by interacting with the players via
   * clients.
   */
  public GameResults runGame() {
    this.sendInitialSetup();
    this.informObserverOfState();

    while (!this.isGameOver()) {
      this.handleTurn();
      this.informObserverOfState();
    }

    this.sendWin();
    this.informObserversOfGameEnd();

    return this.createGameResults();
  }

  /**
   * Execute one in-game turn from start to finish, kicking the active player if it misbehaves.
   */
  private void handleTurn() {
    PlayerAvatar activePlayer = this.game.getActivePlayer();

    Optional<ITurn> potentialTurn = this.getTurn(activePlayer);

    if (potentialTurn.isEmpty()) {
      this.kickPlayerInAll(activePlayer);
      return;
    }

    ITurn turn = potentialTurn.get();

    if (turn.isMove()) {
      Move move = turn.getMove();

      //TODO debug gameplay
      System.out.print(this.getNamesFromAvatars(List.of(activePlayer)) + " ");
      System.out.println(
          "(" + activePlayer + ", goal: " + goalHandler.getPlayerCurrentGoal(activePlayer) + ")");
      System.out.println("Move: " + move.toString());
      System.out.println(this.game.getBoard().toString());

      if (this.isValidMove(move, activePlayer)) {
        this.game.executeTurn(move);
        this.assignNextGoal(activePlayer);
      } else {
        this.kickPlayerInAll(activePlayer);
      }

    } else {
      this.game.skipTurn();
    }

  }

  /**
   * Returns true if the given move is valid for the current player.
   */
  private boolean isValidMove(Move move, PlayerAvatar player) {
    Position moveDestination = move.getMoveDestination();
    Position currentPosition = player.getCurrentPosition();

    ExperimentationBoard expBoard = this.game.getBoard().getExperimentationBoard();

    if (!this.rules.isValidSlideAndInsert(move, this.game.getBoardWidth(),
        this.game.getBoardHeight())) {
      return false;
    }

    return expBoard
        .findReachableTilePositionsAfterSlideAndInsert(move, currentPosition)
        .contains(moveDestination);
  }

  /**
   * Assigns a player their next goal if needed.
   */
  private void assignNextGoal(PlayerAvatar player) {
    if (this.goalHandler.playerReachedGoal(player)) {
      this.goalHandler.nextGoal(player);
      Position nextGoal = this.goalHandler.getPlayerCurrentGoal(player);
      this.sendSetup(player, nextGoal);
    }
  }

  /**
   * Removes the player from the State and Referee
   */
  private void kickPlayerInAll(PlayerAvatar player) {
    this.game.kickPlayer(player);
    this.eliminated.add(player);
  }

  /*
  End of Game Handling:
   */

  /**
   * Returns true if the game is over.
   */
  private boolean isGameOver() {
    boolean status = this.game.isGameOver();
    boolean homeReached = this.goalHandler.anyPlayersHome();
    return status || homeReached;
  }

  /**
   * Returns the list of winners. If the player who ended the game has the most goals, they win.
   * Otherwise, winners are whoever has the most goals. If there is a tie, then it assesses winners
   * off of the distance to their next goal.
   */
  private List<PlayerAvatar> getWinners() {
    List<PlayerAvatar> candidates = this.getWinnerCandidates();

    //If player who ended game has enough goals:
    Optional<PlayerAvatar> potentialGameEnder = this.goalHandler.getPlayerHome();
    if (potentialGameEnder.isPresent() && candidates.contains(potentialGameEnder.get())) {
      return List.of(potentialGameEnder.get());
    }

    return this.assesWinnerDistance(candidates);
  }

  /**
   * Get all the players with the most goals that would be candidates to win the game.
   */
  private List<PlayerAvatar> getWinnerCandidates() {
    List<PlayerAvatar> candidates = new ArrayList<>();
    int maxGoals = 0;
    List<PlayerAvatar> gamePlayers = this.game.getPlayerList();
    for (PlayerAvatar player : gamePlayers) {
      int goalsReached = this.goalHandler.getPlayerGoalCount(player);
      if (goalsReached > maxGoals) {
        maxGoals = goalsReached;
        candidates.clear();
        candidates.add(player);
      } else if (goalsReached == maxGoals) {
        candidates.add(player);
      }
    }
    return candidates;
  }

  /**
   * Given a list of candidates, decides who is closest to their next goal.
   */
  private List<PlayerAvatar> assesWinnerDistance(List<PlayerAvatar> candidates) {
    List<PlayerAvatar> winners = new ArrayList<>();
    double minDistance = Double.MAX_VALUE;
    for (PlayerAvatar player : candidates) {
      Position goal = this.goalHandler.getPlayerCurrentGoal(player);
      Position current = player.getCurrentPosition();
      double distance = Position.getEuclideanDistance(current, goal);
      if (distance < minDistance) {
        minDistance = distance;
        winners.clear();
        winners.add(player);
      } else if (distance == minDistance) {
        winners.add(player);
      }
    }
    return winners;
  }

  /**
   * Creates the results of the game. Includes the winners and the eliminated players.
   */
  private GameResults createGameResults() {
    this.winners.removeAll(this.eliminated);
    List<String> winners = this.getNamesFromAvatars(this.winners);
    List<String> kicked = this.getNamesFromAvatars(this.eliminated);
    return new GameResults(winners, kicked);
  }

  /**
   * Get a list of names that corresponds to the PlayerAvatars
   */
  private List<String> getNamesFromAvatars(List<PlayerAvatar> playerAvatars) {
    List<String> names = new ArrayList<>();
    for (PlayerAvatar player : playerAvatars) {
      names.add(this.playerAvatarToHandler.get(player).getPlayerName());
    }
    Collections.sort(names);
    return names;
  }


  /*
  Player Communication
   */

  /**
   * Send each player a boolean of whether they won or not.
   */
  private void sendWin() {
    List<PlayerAvatar> players = this.game.getPlayerList();
    this.winners.addAll(this.getWinners());
    for (PlayerAvatar player : players) {

      Optional<Boolean> response;
      if (this.winners.contains(player)) {
        response = (this.playerAvatarToHandler.get(player).win(true));
      } else {
        response = (this.playerAvatarToHandler.get(player).win(false));
      }

      if (response.isEmpty()) {
        this.kickPlayerInAll(player);
      }
    }
  }

  /**
   * Sends the initial setup message to the players that includes the starting state.
   */
  private void sendInitialSetup() {
    List<PlayerAvatar> players = this.game.getPlayerList();

    for (PlayerAvatar player : players) {
      Position firstGoal = this.goalHandler.getPlayerCurrentGoal(player);
      StateProjection stateProjection = this.game.getStateProjection();
      this.sendSetup(player, Optional.of(stateProjection), firstGoal);
    }
  }

  /**
   * Sends setups to players to communicate their next goal.
   */
  private void sendSetup(PlayerAvatar player, Optional<StateProjection> stateProjection,
      Position nextGoal) {
    PlayerHandler playerHandler = this.playerAvatarToHandler.get(player);
    Optional<Boolean> outcome = playerHandler.setup(stateProjection, nextGoal);
    if (outcome.isEmpty()) {
      this.kickPlayerInAll(player);
    }
  }

  /**
   * Sends the setup method to the player through the PlayerHandler.
   */
  private void sendSetup(PlayerAvatar player, Position nextGoal) {
    this.sendSetup(player, Optional.empty(), nextGoal);
  }

  /**
   * Asks the given player for their turn and returns the response. Uses PlayerHandler.
   */
  private Optional<ITurn> getTurn(PlayerAvatar player) {
    PlayerHandler playerHandler = this.playerAvatarToHandler.get(player);
    return playerHandler.takeTurn(this.game.getStateProjection());
  }

  /**
   * Wrapper for the IPlayer, adding exception and timeout handling for any IPlayer calls.
   */
  private class PlayerHandler {

    private final IPlayer player;

    /**
     * Creates a PlayerHandler to wrap the player and protect the Referee
     */
    public PlayerHandler(IPlayer player) {
      this.player = player;
    }

    /**
     * Applies a timeout to a method call.
     */
    private <T> Optional<T> timeoutExceptionHandler(Supplier<T> function) {
      try {
        ExecutorService service = Executors.newCachedThreadPool();
        return Optional.of(service.submit(function::get).get(TIMEOUT, TimeUnit.SECONDS));
      } catch (Throwable exception) {
        return Optional.empty();
      }
    }

    public Optional<Boolean> win(boolean w) {
      return this.timeoutExceptionHandler(() -> {
        return this.player.win(w);
      });
    }

    public Optional<ITurn> takeTurn(StateProjection game) {
      return this.timeoutExceptionHandler(() -> {
        return this.player.takeTurn(game);
      });
    }

    public Optional<Boolean> setup(Optional<StateProjection> state, Position goal) {
      return this.timeoutExceptionHandler(() -> {
        return this.player.setup(state, goal);
      });
    }

    public Optional<IBoard> proposeBoard(int rows, int columns) {
      return this.timeoutExceptionHandler(() -> {
        return this.player.proposeBoard(rows, columns);
      });
    }

    // Should be stored locally in the ProxyPlayer - doesn't require a network call, and thus can't time out
    public String getPlayerName() {
      return this.player.getName();
    }


  }

  /*
  Observer Functionality
   */

  @Override
  public void addObserver(IObserver observer) {
    this.observers.add(observer);
  }

  private void informObserverOfState() {
    for (IObserver observer : this.observers) {
      observer.update(this.game.getStateProjection());
    }
  }

  private void informObserversOfGameEnd() {

    for (IObserver observer : this.observers) {
      observer.gameOver();
    }
  }

//  private class ObserverHandler implements IObserver {
//
//    IObserver observer;
//
//    public ObserverHandler(IObserver observer) {
//      this.observer = observer;
//    }
//
//    private <T> Optional<T> timeoutExceptionHandler(Supplier<T> function) {
//      try {
//        ExecutorService service = Executors.newCachedThreadPool();
//        return Optional.of(service.submit(function::get).get(TIMEOUT, TimeUnit.SECONDS));
//      } catch (Throwable exception) {
//        return Optional.empty();
//      }
//    }
//
//    @Override
//    public void update(ObserverGameProjection state) {
//      this.timeoutExceptionHandler(() -> {
//        return this.observer.update(state);
//      });
//    }
//
//    @Override
//    public void gameOver() {
//
//    }
//  }
}
