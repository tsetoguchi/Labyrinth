package referee;

import model.Position;

import java.awt.Color;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import model.board.ExperimentationBoard;
import model.board.IBoard;
import model.projections.StateProjection;
import model.state.GameResults;
import model.state.IState;
import model.state.PlayerAvatar;
import observer.Controller.IObserver;
import player.IPlayer;
import java.util.*;

import static referee.PlayerResult.*;

/**
 * Manages a game by running it to completion, kicking any players that misbehave, fail to respond,
 * or throw an error, and returns the result of the game to the remaining players.
 */
public class Referee implements IReferee {

  private IState game;


  private final IRules rules;
  private Map<PlayerAvatar, PlayerHandler> playerAvatarToHandler;

  private final GoalHandler goalHandler;

  // TODO: Make observer handler
  private final List<IObserver> observers;

  // all eliminated players so far, including cheaters.
  private final List<PlayerAvatar> eliminated;

  private static final int TIMEOUT = 20;

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
   * Runs the game this referee is moderating to completion by interacting with the players via
   * clients.
   */
  public GameResults runGame() {
    this.informObserverOfState();
    this.setupPlayers();

    while (this.isGameOver()) {
      this.handleTurn();
    }

    this.informPlayersOfGameEnd();
    this.informObserversOfGameEnd();

    return this.createGameResults();

  }

  private GameResults createGameResults() {
    List<String> winners = this.getNamesFromAvatars(this.getWinners());
    List<String> kicked = this.getNamesFromAvatars(this.eliminated);
    return new GameResults(winners, kicked);
  }

  private List<String> getNamesFromAvatars(List<PlayerAvatar> playerAvatars) {
    List<String> names = new ArrayList<>();
    for (PlayerAvatar player : playerAvatars) {
      names.add(this.playerAvatarToHandler.get(player).getPlayerName());
    }
    Collections.sort(names);
    return names;
  }

  public void resume(IState game, List<IPlayer> iPlayerInterfaces) {
    this.playerAvatarToHandler = this.mapPlayerAvatarsToPlayerHandlers(game, iPlayerInterfaces);
    this.game = game;
    this.runGame();
  }

  private boolean isGameOver(){
    boolean status = this.game.isGameOver();
    boolean homeReached = this.goalHandler.playerReachedHome();
    return status || homeReached;
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
    Optional turnWrapper = this.getPlanFromPlayer(activePlayer);
    if (turnWrapper.isException()) { // Player raised an exception when asked for a turn
      this.kickPlayerInGameAndRef(activePlayer);
      return;
    }
    Optional<Move> turnPlan = turnWrapper.getTurnPlan();
    if (this.isValidTurnPlan(turnPlan)) {

      // Pass
      if (turnPlan.isEmpty()) {
        this.game.skipTurn();
      }

      // Perform Move
      else {

        Move move = turnPlan.get();

        Position moveDestination = turnPlan.get().getMoveDestination();
        if (this.rules.isValidSlideAndInsert(move, this.game.getBoardWidth(), this.game.getBoardHeight())) {
          this.game.executeTurn(move);
        } else {
          this.kickPlayerInGameAndRef(activePlayer);
        }

        this.assignNextGoal(activePlayer);
        this.informObserverOfState();
      }
    } else {

      this.kickPlayerInGameAndRef(activePlayer);
    }
  }

  private TurnWrapper getPlanFromPlayer(PlayerAvatar player) {
    PlayerHandler playerHandler = this.playerAvatarToHandler.get(player);
    Optional<ITurn> playerTurn = playerHandler.takeTurn(
        this.game.getStateProjection());

    if (playerTurn.isEmpty()) {
      // Player timed out or has an exception
      return new TurnWrapper(Optional.empty(), true);
    } else {
      // Player plans to Pass for their Move
      return new TurnWrapper(playerTurn.get(), false);
    }
  }

  private boolean isValidTurnPlan(Optional<Move> turnPlan) {
    if (turnPlan.isEmpty()) {
      return true;
    }

    Move move = turnPlan.get();
    Position moveDestination = turnPlan.get().getMoveDestination();
    Position currentPosition = this.game.getActivePlayer().getCurrentPosition();

    ExperimentationBoard expBoard = this.game.getBoard().getExperimentationBoard();

    if (!this.rules.isValidSlideAndInsert(move, this.game.getBoardWidth(),
        this.game.getBoardHeight())) {
      return false;
    }
    return expBoard
        .findReachableTilePositionsAfterSlideAndInsert(move, currentPosition)
        .contains(moveDestination);
  }

  private void informPlayersOfGameEnd() {
    Map<PlayerAvatar, PlayerResult> playerResults = this.getResultsForPlayers();

    for (PlayerAvatar player : this.game.getPlayerList()) {
      PlayerResult resultForPlayer = playerResults.get(player);

      if (resultForPlayer.equals(PlayerResult.WINNER)) {
        Optional<Boolean> timeout = (this.playerAvatarToHandler.get(player).win(true));
        if (timeout.isEmpty()) {
          this.kickPlayerInGameAndRef(player);
        }
      } else {
        Optional<Boolean> timeout = this.playerAvatarToHandler.get(player).win(false);
        if (timeout.isEmpty()) {
          this.kickPlayerInGameAndRef(player);
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
    List<PlayerAvatar> candidates = new ArrayList<>();
    int maxGoals = 0;
    for(PlayerAvatar player : this.game.getPlayerList()){
      int goalsReached = this.goalHandler.getPlayerGoalCount(player);
      if(goalsReached > maxGoals){
        maxGoals = goalsReached;
        candidates.clear();
        candidates.add(player);
      } else if(goalsReached == maxGoals){
        candidates.add(player);
      }
    }

    List<PlayerAvatar> winners = new ArrayList<>();
    double minDistance = Double.MAX_VALUE;
    for(PlayerAvatar player : candidates){
      Position goal = this.goalHandler.getPlayerCurrentGoal(player);
      Position current = player.getCurrentPosition();
      double distance = Position.getEuclideanDistance(current, goal);
      if(distance < minDistance){
        minDistance = distance;
        winners.clear();
        winners.add(player);
      } else if(distance == minDistance){
        winners.add(player);
      }
    }

    return winners;
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
      Position goalPosition = this.goalHandler.getPlayerCurrentGoal(player);
      playerDistanceMap.put(player, Position.getEuclideanDistance(playerPosition, goalPosition));
    }
    return playerDistanceMap;
  }


  private void assignNextGoal(PlayerAvatar player) {
    if (this.goalHandler.playerReachedGoal(player)) {

      this.goalHandler.nextGoal(player);

      this.playerAvatarToHandler.get(player).setup(Optional.empty(),
          this.goalHandler.getPlayerCurrentGoal(player));
    }
  }

  private void setupPlayers() {
    List<PlayerAvatar> playersToBeKicked = new ArrayList<>();

    for (PlayerAvatar player : this.game.getPlayerList()) {
      PlayerHandler playerHandler = this.playerAvatarToHandler.get(player);
      Optional<Boolean> outcome = playerHandler.setup(Optional.of(
              this.game.getStateProjection()),
          this.goalHandler.getPlayerCurrentGoal(player));

      if (outcome.isEmpty()) {
        playersToBeKicked.add(player);
      }
    }

    for (PlayerAvatar player : playersToBeKicked) {
      this.kickPlayerInGameAndRef(player);
    }
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
    this.playerAvatarToHandler.remove(player);
    this.game.kickPlayer(player);
    this.eliminated.add(player);
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
