package referee;

import model.Position;

import java.awt.Color;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import model.board.DefaultRules;
import model.board.ExperimentationBoard;
import model.board.IBoard;
import model.board.IRules;
import model.projections.StateProjection;
import model.state.GameResults;
import model.state.GameStatus;
import model.state.IState;
import model.state.PlayerAvatar;
import model.state.State;
import observer.Controller.IObserver;
import player.IPlayer;
import java.util.*;

import static model.state.GameStatus.IN_PROGRESS;
import static referee.PlayerResult.*;

/**
 * Manages a game by running it to completion, kicking any players that misbehave, fail to respond,
 * or throw an error, and returns the result of the game to the remaining players.
 */
public class Referee implements IReferee {

  private IState game;

  private final IRules rules;
  /**
   * Stores which client should be used to talk to each Player.
   **/
  private Map<PlayerAvatar, PlayerHandler> playerAvatarToHandler;
  private final List<PlayerAvatar> playersCollectedTreasures;

  private Map<PlayerAvatar, Position> currentGoals;
  private Map<PlayerAvatar, Integer> goalCount;
  private Queue<Position> potentialGoals;

  // TODO: Make observer handler
  private final List<IObserver> observers;

  // the eliminated players so far, including cheaters.
  private final List<IPlayer> eliminated;

  private static final int TIMEOUT = 20;

  /**
   * Creates a referee and assigns it a game to moderate, as well as a list of clients which should
   * be used to interface with players. The order of the clients in the list must correspond to the
   * order of players in the State. Accepts an optional observer which will be updated as the game
   * progresses.
   */
  public Referee(IState game, List<IPlayer> players,
      List<IObserver> observers, IRules rules) {
    this.game = game;
    this.rules = rules;
    this.playerAvatarToHandler = this.mapPlayerAvatarsToPlayerHandlers(game, players);
    this.playersCollectedTreasures = new ArrayList<>();
    this.observers = observers;
    this.eliminated = new ArrayList<>();
  }

  public Referee(IState game, List<IPlayer> players) {
    this(game, players, List.of(),
        new DefaultRules());
  }

  /**
   * Initialize a Referee with a just a set of proxy players, building a new randomized State from
   * scratch.
   */
  public Referee(List<IPlayer> players, IRules rules) {
    List<IBoard> proposedBoards = new ArrayList<>();
    List<Color> uniqueColors = this.generateUniqueColors(players.size());

    // get proposed board
    List<PlayerHandler> playerHandlers = this.interfaceToHandlers(players, uniqueColors);
    List<PlayerHandler> invalidProposals = new ArrayList<>();
    for (PlayerHandler playerHandler : playerHandlers) {
      Optional<IBoard> board = playerHandler
          .proposeBoard(this.game.getBoard().getHeight(), this.game.getBoard().getWidth());
      if (board.isPresent()) {
        proposedBoards.add(board.get());
      } else {
        // proposal was invalid
        invalidProposals.add(playerHandler);
      }
    }
    for (PlayerHandler playerHandler : invalidProposals) {
      playerHandlers.remove(playerHandler);
    }
    IBoard board = proposedBoards.get(new Random().nextInt(proposedBoards.size() - 1));

    List<Position> immovablePositions = this.immovablePositionsForBoard(board);
    Collections.shuffle(immovablePositions);

    List<PlayerAvatar> playerAvatars = new ArrayList<>();
    List<Position> homes = new ArrayList<>(immovablePositions);
    Collections.shuffle(immovablePositions);

    List<Position> goals = new ArrayList<>(immovablePositions);

    for (int i = 0; i < uniqueColors.size(); i++) {
      playerAvatars.add(new PlayerAvatar(uniqueColors.get(i), goals.get(i), homes.get(i)));
    }
    IState game = new State(board, playerAvatars);
    this.game = game;
    this.rules = rules;
    this.playerAvatarToHandler = this.mapPlayerAvatarsToPlayerHandlers(game, players);
    this.playersCollectedTreasures = new ArrayList<>();
    this.observers = new ArrayList<>();
    this.eliminated = new ArrayList<>();
  }

  /**
   * Runs the game this referee is moderating to completion by interacting with the players via
   * clients.
   */
  public GameResults runGame() {
    this.informObserverOfState();
    this.setupPlayers();

    GameStatus gameStatus = this.game.getGameStatus();
    while (gameStatus == IN_PROGRESS) {
      this.handleTurn();
      gameStatus = this.game.getGameStatus();
    }

    this.informPlayersOfGameEnd(gameStatus);
    this.informObserversOfGameEnd();

    return new GameResults(this.getNamesFromAvatars(this.getWinners()),
        this.getNamesFromRefereePlayerInterfaces(this.eliminated));
  }

  public void resume(IState game, List<IPlayer> iPlayerInterfaces) {
    this.playerAvatarToHandler = this.mapPlayerAvatarsToPlayerHandlers(game, iPlayerInterfaces);
    this.game = game;
    this.runGame();
  }

  @Override
  public void addObserver(IObserver observer) {
    this.observers.add(observer);
  }

  /**
   * Execute one in-game turn from start to finish, kicking the active player if it misbehaves.
   */
  private void handleTurn() {
    PlayerAvatar activePlayer = this.game.getActivePlayer();
    TurnWrapper turnWrapper = this.getPlanFromPlayer(activePlayer);
    if (turnWrapper.isException()) { // Player raised an exception when asked for a turn
      this.kickPlayerInGameAndRef(activePlayer);
      return;
    }
    Optional<Turn> turnPlan = turnWrapper.getTurnPlan();
    if (this.isValidTurnPlan(turnPlan)) {

      // Pass
      if (turnPlan.isEmpty()) {
        this.game.skipTurn();
      }

      // Perform Turn
      else {

        Turn turn = turnPlan.get();

        Position moveDestination = turnPlan.get().getMoveDestination();
        if (this.rules.isValidSlideAndInsert(turn, this.game.getBoardWidth(),
            this.game.getBoardHeight())) {
          this.game.slideAndInsert(turn);
          this.game.moveActivePlayer(moveDestination);
        } else {
          this.kickPlayerInGameAndRef(activePlayer);
        }

        this.remindPlayersReturnHome();
        this.informObserverOfState();
      }
    } else {

      PlayerAvatar toBeKickedPlayer = this.game.getActivePlayer();
      this.eliminated.add(this.playerAvatarToHandler.get(toBeKickedPlayer).getPlayerClient());
      this.playerAvatarToHandler.remove(toBeKickedPlayer);
      this.game.kickPlayer(toBeKickedPlayer);
    }
  }

  private TurnWrapper getPlanFromPlayer(PlayerAvatar player) {
    System.out.println(player);
    PlayerHandler playerHandler = this.playerAvatarToHandler.get(player);
    System.out.println(this.playerAvatarToHandler);
    Optional<Optional<Turn>> playerTurn = playerHandler.takeTurn(
        new StateProjection(this.game, player, this.game.getPreviousSlideAndInsert()));

    if (playerTurn.isEmpty()) {
      // Player timed out or has an exception
      return new TurnWrapper(Optional.empty(), true);
    } else {
      // Player plans to Pass for their Turn
      return new TurnWrapper(playerTurn.get(), false);
    }
  }

  private boolean isValidTurnPlan(Optional<Turn> turnPlan) {
    if (turnPlan.isEmpty()) {
      return true;
    }

    Turn turn = turnPlan.get();
    Position moveDestination = turnPlan.get().getMoveDestination();
    Position currentPosition = this.game.getActivePlayer().getCurrentPosition();

    ExperimentationBoard expBoard = this.game.getBoard().getExperimentationBoard();

    if (!this.rules.isValidSlideAndInsert(turn, this.game.getBoardWidth(),
        this.game.getBoardHeight())) {
      return false;
    }
    return expBoard
        .findReachableTilePositionsAfterSlideAndInsert(turn, currentPosition)
        .contains(moveDestination);
  }

  private void informPlayersOfGameEnd(GameStatus gameStatus) {
    Map<PlayerAvatar, PlayerResult> playerResults = this.getResultsForPlayers();

    for (PlayerAvatar player : this.game.getPlayerList()) {
      PlayerResult resultForPlayer = playerResults.get(player);
      this.playerAvatarToHandler.get(player).informGameEnd(gameStatus, resultForPlayer);

      if (resultForPlayer.equals(PlayerResult.WINNER)) {
        Optional<Boolean> timeout = (this.playerAvatarToHandler.get(player).win(true));
        if (timeout.isEmpty()) {
          this.kickPlayerInGameAndRef(player.getColor());
        }
      } else {
        Optional<Boolean> timeout = this.playerAvatarToHandler.get(player).win(false);
        if (timeout.isEmpty()) {
          this.kickPlayerInGameAndRef(player.getColor());
        }
      }
    }
  }

  private Map<PlayerAvatar, PlayerResult> getResultsForPlayers() {
    List<PlayerAvatar> winners = this.getWinners();

    Map<PlayerAvatar, PlayerResult> results = new HashMap<>();
    for (PlayerAvatar player : this.game.getPlayerList()) {
      if (winners.contains(player)) {
        results.put(player, WINNER);
      } else {
        results.put(player, LOSER);
      }
    }
    return results;
  }

  private List<PlayerAvatar> getWinners() {
    List<PlayerAvatar> winners = new ArrayList<>();

    List<PlayerAvatar> playersThatReachedGoal = new ArrayList<>();
    for (PlayerAvatar player : this.game.getPlayerList()) {
      if (player.hasReachedGoal()) {
        playersThatReachedGoal.add(player);
      }
    }

    Map<PlayerAvatar, Double> distancesToCompare;
    List<PlayerAvatar> contenders;

    if (playersThatReachedGoal.size() > 0) {
      distancesToCompare = this.mapPlayersToDistanceFromHome();
      contenders = playersThatReachedGoal;
    } else {
      distancesToCompare = this.mapPlayersToDistanceFromGoal();
      contenders = this.game.getPlayerList();
    }
    double minDistance = Double.MAX_VALUE;

    for (PlayerAvatar contender : contenders) {
      double contenderDistanceFromHome = distancesToCompare.get(contender);
      if (contenderDistanceFromHome < minDistance) {
        winners = new ArrayList<>(); // clear previous winners who were further
        winners.add(contender);
        minDistance = contenderDistanceFromHome;
      }
      if (contenderDistanceFromHome == minDistance) {
        winners.add(contender);
      }
    }
    return winners;
  }

  public List<String> getNamesFromAvatars(List<PlayerAvatar> playerAvatars) {
    List<String> names = new ArrayList<>();
    for (PlayerAvatar player : playerAvatars) {
      names.add(this.playerAvatarToHandler.get(player).getPlayerName());
    }

    Collections.sort(names);
    return names;
  }

  public List<String> getEliminatedNames() {
    List<String> names = new ArrayList<>();
    for (IPlayer playerInterface : this.eliminated) {
      names.add(playerInterface.getName());
    }
    Collections.sort(names);
    return names;
  }

  private List<String> getNamesFromRefereePlayerInterfaces(List<IPlayer> playerInterfaces) {
    List<String> names = new ArrayList<>();
    for (IPlayer playerInterface : playerInterfaces) {
      names.add(playerInterface.getName());
    }
    return names;
  }


  /**
   * Maps the distance of every player to their home Tile.
   */
  private Map<PlayerAvatar, Double> mapPlayersToDistanceFromHome() {
    Map<PlayerAvatar, Double> playerDistanceMap = new HashMap<>();
    List<PlayerAvatar> playerList = this.game.getPlayerList();
    for (PlayerAvatar player : playerList) {
      Position playerPosition = player.getCurrentPosition();
      Position homePosition = player.getHome();
      playerDistanceMap.put(player, Position.getEuclideanDistance(playerPosition, homePosition));
    }
    return playerDistanceMap;
  }

  /**
   * Maps the distance of every player to their goal Tile. If the goal Tile is the spare Tile, the
   * distance is considered to be the greatest distance.
   */
  private Map<PlayerAvatar, Double> mapPlayersToDistanceFromGoal() {
    Map<PlayerAvatar, Double> playerDistanceMap = new HashMap<>();
    List<PlayerAvatar> playerList = this.game.getPlayerList();
    for (PlayerAvatar player : playerList) {
      Position playerPosition = player.getCurrentPosition();
      Position goalPosition = player.getGoal();
      playerDistanceMap.put(player, Position.getEuclideanDistance(playerPosition, goalPosition));
    }
    return playerDistanceMap;
  }

  private Map<PlayerAvatar, IPlayer> mapPlayerAvatarsToPlayerClients(State state,
      List<IPlayer> iPlayerInterfaces) {
    Map<PlayerAvatar, IPlayer> map = new HashMap<>();
    if (iPlayerInterfaces.size() != state.getPlayerList().size()) {
      throw new IllegalArgumentException("Amount of clients and players do not match.");
    }
    for (int i = 0; i < iPlayerInterfaces.size(); i++) {
      map.put(state.getPlayerList().get(i), iPlayerInterfaces.get(i));
    }
    return map;
  }

  private void remindPlayersReturnHome() {
    for (PlayerAvatar player : this.game.getPlayerList()) {
      if (player.hasReachedGoal() && !this.playersCollectedTreasures.contains(player)) {
        this.playerAvatarToHandler.get(player).setup(Optional.empty(), player.getHome());
        this.playersCollectedTreasures.add(player);
      }
    }
  }

  private void setupPlayers() {
    List<Color> playersToBeKicked = new ArrayList<>();

    for (PlayerAvatar player : this.game.getPlayerList()) {
      PlayerHandler playerHandler = this.playerAvatarToHandler.get(player);
      Optional<Boolean> outcome = playerHandler.setup(Optional.of(
              new StateProjection(this.game, player, this.game.getPreviousSlideAndInsert())),
          player.getGoal());

      if (outcome.isEmpty()) {
        playersToBeKicked.add(player.getColor());
      }
    }
    for (Color color : playersToBeKicked) {
      System.out.println("Kick players in setup");
      this.kickPlayerInGameAndRef(color);
    }
  }

  private void informObserverOfState() {
    for (IObserver observer : this.observers) {
      observer.update(new StateProjection(this.game));
    }
  }

  private void informObserversOfGameEnd() {

    for (IObserver observer : this.observers) {
      observer.gameOver();
    }
  }

  private Map<PlayerAvatar, PlayerHandler> mapPlayerAvatarsToPlayerHandlers(IState game,
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

  /**
   * Removes the player with the specified color from the State and Referee
   */
  private void kickPlayerInGameAndRef(PlayerAvatar player) {
    System.out.println("Kicked Player: ");
    this.playerAvatarToHandler.remove(player);
    this.game.kickPlayer(player);
  }


  private List<Position> immovablePositionsForBoard(IBoard board) {

    List<Position> immovablePositions = new ArrayList<>();
    // Iterate through immovable rows
    for (int row = 0; row < board.getHeight(); row++) {
      for (int col = 0; col < board.getWidth(); col++) {
        Position currentPosition = new Position(row, col);
        if (this.rules.immovablePosition(currentPosition)) {
          immovablePositions.add(currentPosition);
        }
      }
    }
    return immovablePositions;
  }

  private List<Color> generateUniqueColors(int numberOfColors) {
    Set<Color> colors = new HashSet<>();
    Random random = new Random();
    while (colors.size() < numberOfColors) {
      colors.add(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
    }

    return new ArrayList<>(colors);
  }

  private List<PlayerHandler> interfaceToHandlers(List<IPlayer> playerInterfaces,
      List<Color> colors) {

    if (playerInterfaces.size() != colors.size()) {
      throw new IllegalArgumentException("Amount of players do not match amount of colors.");
    }

    List<PlayerHandler> playerHandlers = new ArrayList<>();
    for (int i = 0; i < playerInterfaces.size(); i++) {
      playerHandlers.add(new PlayerHandler(playerInterfaces.get(i)));
    }
    return playerHandlers;
  }

  /**
   * Wrapper for the Client class, adding exception and timeout handling for any Client calls.
   */
  private class PlayerHandler {

    private final IPlayer player;

    public PlayerHandler(IPlayer player) {
      this.player = player;
    }

    private <T> Optional<T> timeoutExceptionHandler(Supplier<T> function) {
      try {
        ExecutorService service = Executors.newCachedThreadPool();
        return Optional.of(service.submit(function::get).get(TIMEOUT, TimeUnit.SECONDS));
      } catch (Throwable exception) {
        eliminated.add(this.player);
        return Optional.empty();
      }
    }

    public Optional<Boolean> win(boolean w) {
      return this.timeoutExceptionHandler(() -> {
        return this.player.win(w);
      });
    }

    public Optional<Optional<Turn>> takeTurn(StateProjection game) {
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

    public IPlayer getPlayerClient() {
      return this.player;
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
