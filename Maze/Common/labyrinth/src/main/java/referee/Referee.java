package referee;

import game.controller.IObserver;
import game.model.*;
import game.model.projections.ObserverGameProjection;
import game.model.projections.PlayerGameProjection;

import java.awt.Color;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import player.TurnPlan;
import player.TurnWrapper;
import referee.clients.RefereePlayerInterface;

import java.util.*;

import static game.model.GameStatus.*;
import static referee.PlayerResult.*;

/**
 * Manages a game by running it to completion, kicking any players that misbehave, fail to respond,
 * or throw an error, and returns the result of the game to the remaining players.
 */
public class Referee implements IReferee {

    private PrivateGameState game;
    /**
     * Stores which client should be used to talk to each Player.
     **/
    //private Map<PlayerAvatar, PlayerClient> playerAvatarToClient;
    private Map<PlayerAvatar, PlayerHandler> playerAvatarToHandler;
    private final List<PlayerAvatar> playersCollectedTreasures;
    private final List<IObserver> observers;

    // the eliminated players so far, including cheaters.
    private final List<RefereePlayerInterface> eliminated;

    private static final int TIMEOUT = 20;

    /**
     * Creates a referee and assigns it a game to moderate, as well as a list of clients which should
     * be used to interface with players. The order of the clients in the list must correspond to the
     * order of players in the Game. Accepts an optional observer which will be updated as the game
     * progresses.
     */
    public Referee(PrivateGameState game, List<RefereePlayerInterface> players,
                   List<IObserver> observers) {
        this.game = game;
        this.playerAvatarToHandler = this.mapPlayerAvatarsToPlayerHandlers(game, players);
        this.playersCollectedTreasures = new ArrayList<>();
        this.observers = observers;
        this.eliminated = new ArrayList<>();
    }

    public Referee(PrivateGameState game, List<RefereePlayerInterface> players) {
        this(game, players, List.of());
    }

    /**
     * Initialize a Referee with a just a set of proxy players, building a new randomized Game from scratch.
     */
    public Referee(List<RefereePlayerInterface> players) {
        List<Board> proposedBoards = new ArrayList<>();
        List<Color> uniqueColors = this.generateUniqueColors(players.size());

        // get proposed board
        List<PlayerHandler> playerHandlers = this.interfaceToHandlers(players, uniqueColors);
        List<PlayerHandler> invalidProposals = new ArrayList<>();
        for (PlayerHandler playerHandler : playerHandlers) {
            Optional<Board> board = playerHandler.proposeBoard(game.getBoard().getHeight(),game.getBoard().getWidth());
            if (board.isPresent()) {
                proposedBoards.add(board.get());
            }
            else {
                // proposal was invalid
                invalidProposals.add(playerHandler);
            }
        }
        for (PlayerHandler playerHandler : invalidProposals) {
            playerHandlers.remove(playerHandler);
        }
        Board board = proposedBoards.get(new Random().nextInt(proposedBoards.size() - 1));

        List<Position> immovablePositions = this.immovablePositionsForBoard(board);
        Collections.shuffle(immovablePositions);

        List<PlayerAvatar> playerAvatars = new ArrayList<>();
        List<Position> homes = new ArrayList<>();
        for (Position home : immovablePositions) {
            homes.add(home);
        }
        Collections.shuffle(immovablePositions);

        List<Position> goals = new ArrayList<>();
        for (Position goal : immovablePositions) {
            goals.add(goal);
        }

        for (int i = 0; i < uniqueColors.size(); i++) {
            playerAvatars.add(new PlayerAvatar(uniqueColors.get(i), goals.get(i), homes.get(i)));
        }
        PrivateGameState game = new Game(board, playerAvatars);
        this.game = game;
        this.playerAvatarToHandler = this.mapPlayerAvatarsToPlayerHandlers(game, players);
        this.playersCollectedTreasures = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.eliminated = new ArrayList<>();
    }

    private List<Position> immovablePositionsForBoard(Board board) {
        List<Integer> immovableRowIndices = new ArrayList<>();
        List<Integer> immovableColIndices = new ArrayList<>();
        // Iterate through immovable rows
        for (int i = 0; i < board.getHeight(); i++) {
            if (!board.getRules().isValidSlideAndInsert(Direction.RIGHT, i, 0)) {
                immovableRowIndices.add(i);
            }
        }
        for (int i = 0; i < board.getWidth(); i++) {
            if (!board.getRules().isValidSlideAndInsert(Direction.DOWN, i, 0)) {
                immovableColIndices.add(i);
            }
        }

        List<Position> immovablePositions = new ArrayList<>();
        for (int row = 0; row < immovableRowIndices.size(); row++) {
            for (int col = 0; col < immovableColIndices.size(); col++) {
                immovablePositions.add(new Position(row, col));
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

    private List<PlayerHandler> interfaceToHandlers(List<RefereePlayerInterface> playerInterfaces, List<Color> colors) {

        if (playerInterfaces.size() != colors.size()) {
            throw new IllegalArgumentException("Amount of players do not match amount of colors.");
        }

        List<PlayerHandler> playerHandlers = new ArrayList<>();
        for (int i = 0; i < playerInterfaces.size(); i++) {
            playerHandlers.add(new PlayerHandler(colors.get(i), playerInterfaces.get(i)));
        }
        return playerHandlers;
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

        return new GameResults(this.getNamesFromAvatars(this.getWinners()), this.getNamesFromRefereePlayerInterfaces(this.eliminated));
    }

    public void resume(PrivateGameState game, List<RefereePlayerInterface> refereePlayerInterfaces) {
        this.playerAvatarToHandler = this.mapPlayerAvatarsToPlayerHandlers(game, refereePlayerInterfaces);
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
            this.kickPlayerInGameAndRef(activePlayer.getColor());
            return;
        }
        Optional<TurnPlan> turnPlan = turnWrapper.getTurnPlan();
        if (this.isValidTurnPlan(turnPlan)) {

            // Pass
            if (turnPlan.isEmpty()) {
                this.game.skipTurn();
            }

            // Perform Turn
            else {

                Direction slideDirection = turnPlan.get().getSlideDirection();
                int slideIndex = turnPlan.get().getSlideIndex();
                int rotations = turnPlan.get().getSpareTileRotations();
                Position moveDestination = turnPlan.get().getMoveDestination();

                this.game.slideAndInsert(slideDirection, slideIndex, rotations);
                this.game.moveActivePlayer(moveDestination);
                this.remindPlayersReturnHome();
                this.informObserverOfState();
            }
        } else {

            PlayerAvatar toBeKickedPlayer = this.game.getActivePlayer();
            this.eliminated.add(this.playerAvatarToHandler.get(toBeKickedPlayer).getPlayerClient());
            this.playerAvatarToHandler.remove(toBeKickedPlayer);
            this.game.kickActivePlayer();
        }
    }

    private TurnWrapper getPlanFromPlayer(PlayerAvatar player) {
        PlayerHandler playerHandler = this.playerAvatarToHandler.get(player);
        Optional<Optional<TurnPlan>> playerTurn = playerHandler.takeTurn(
                new PlayerGameProjection(this.game, player, this.game.getPreviousSlideAndInsert()));

        if (playerTurn.isEmpty()) {
            // Player timed out or has an exception
            return new TurnWrapper(Optional.empty(), true);
        } else {
            // Player plans to Pass for their Turn
            return new TurnWrapper(playerTurn.get(), false);
        }
    }

    private boolean isValidTurnPlan(Optional<TurnPlan> turnPlan) {
        if (turnPlan.isEmpty()) {
            return true;
        }
        Direction slideDirection = turnPlan.get().getSlideDirection();
        int slideIndex = turnPlan.get().getSlideIndex();
        int rotations = turnPlan.get().getSpareTileRotations();
        Position moveDestination = turnPlan.get().getMoveDestination();

        ExperimentationBoard expBoard = this.game.getBoard().getExperimentationBoard();

        if (!this.game.getBoard().getRules()
                .isValidSlideAndInsert(slideDirection, slideIndex, rotations)) {
            return false;
        }
        return expBoard
                .findReachableTilePositionsAfterSlideAndInsert(slideDirection, slideIndex, rotations,
                        this.game.getActivePlayer().getCurrentPosition())
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
        for (RefereePlayerInterface playerInterface : this.eliminated) {
            names.add(playerInterface.getPlayerName());
        }
        Collections.sort(names);
        return names;
    }

    private List<String> getNamesFromRefereePlayerInterfaces(List<RefereePlayerInterface> playerInterfaces) {
        List<String> names = new ArrayList<>();
        for (RefereePlayerInterface playerInterface : playerInterfaces) {
            names.add(playerInterface.getPlayerName());
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
            Position homePosition = player.getHomePosition();
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
            Position goalPosition = this.game.getActivePlayer().getGoalPosition();
            playerDistanceMap.put(player, Position.getEuclideanDistance(playerPosition, goalPosition));
        }
        return playerDistanceMap;
    }

    private Map<PlayerAvatar, RefereePlayerInterface> mapPlayerAvatarsToPlayerClients(Game game,
                                                                                      List<RefereePlayerInterface> refereePlayerInterfaces) {
        Map<PlayerAvatar, RefereePlayerInterface> map = new HashMap<>();
        if (refereePlayerInterfaces.size() != game.getPlayerList().size()) {
            throw new IllegalArgumentException("Amount of clients and players do not match.");
        }
        for (int i = 0; i < refereePlayerInterfaces.size(); i++) {
            map.put(game.getPlayerList().get(i), refereePlayerInterfaces.get(i));
        }
        return map;
    }

    private void remindPlayersReturnHome() {
        for (PlayerAvatar player : this.game.getPlayerList()) {
            if (player.hasReachedGoal() && !this.playersCollectedTreasures.contains(player)) {
                this.playerAvatarToHandler.get(player).returnHome(player.getHomePosition());
                this.playersCollectedTreasures.add(player);
            }
        }
    }

    private void setupPlayers() {
        List<Color> playersToBeKicked = new ArrayList<>();
        for (PlayerAvatar player : this.game.getPlayerList()) {
            PlayerHandler playerHandler = this.playerAvatarToHandler.get(player);
            Optional<Boolean> outcome = playerHandler.setup(Optional.of(
                    new PlayerGameProjection(this.game, player, this.game.getPreviousSlideAndInsert())),
                    player.getGoalPosition());

            if (outcome.isEmpty()) {
                playersToBeKicked.add(player.getColor());
            }
        }
        for (Color color : playersToBeKicked) {
            this.kickPlayerInGameAndRef(color);
        }
    }

    private void informObserverOfState() {
        for (IObserver observer : this.observers) {
            observer.update(new ObserverGameProjection(this.game));
        }
    }

    private void informObserversOfGameEnd() {

        for (IObserver observer : this.observers) {
            observer.gameOver();
        }
    }

    private Map<PlayerAvatar, PlayerHandler> mapPlayerAvatarsToPlayerHandlers(PrivateGameState game,
                                                                              List<RefereePlayerInterface> refereePlayerInterfaces) {
        if (refereePlayerInterfaces.size() != game.getPlayerList().size()) {
            throw new IllegalArgumentException("Amount of clients and players do not match.");
        }

        Map<PlayerAvatar, PlayerHandler> playersToHandlers = new HashMap<>();

        for (int i = 0; i < game.getPlayerList().size(); i++) {
            PlayerAvatar playerAvatar = game.getPlayerList().get(i);
            PlayerHandler playerHandler = new PlayerHandler(playerAvatar.getColor(),
                    refereePlayerInterfaces.get(i));
            playersToHandlers.put(playerAvatar, playerHandler);
        }
        return playersToHandlers;
    }

    /**
     * Removes the player with the specified color from the Game and Referee
     */
    private void kickPlayerInGameAndRef(Color color) {
        this.kickPlayer(color);
        this.game.kickActivePlayer();
    }

    private void kickPlayer(Color color) {
        for (PlayerAvatar player : this.playerAvatarToHandler.keySet()) {
            if (player.getColor().equals(color)) {

                this.playerAvatarToHandler.remove(player);
                break;
            }
        }
    }


    /**
     * Wrapper for the PlayerClient class, adding exception and timeout handling for any PlayerClient
     * calls.
     */
    private class PlayerHandler {

        private final Color color;
        private final RefereePlayerInterface player;

        public PlayerHandler(Color color, RefereePlayerInterface player) {
            this.color = color;
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

        public Optional<Optional<TurnPlan>> takeTurn(PlayerGameProjection game) {
            return this.timeoutExceptionHandler(() -> {
                return this.player.takeTurn(game);
            });
        }

        public Optional<Boolean> setup(Optional<PlayerGameProjection> state, Position goal) {
            return this.timeoutExceptionHandler(() -> {
                return this.player.setup(state, goal);
            });
        }

        public Optional<Board> proposeBoard(int rows, int columns) {
            return this.timeoutExceptionHandler(() -> {
                return this.player.proposeBoard(rows, columns);
            });
        }

        // TODO: Understand more about remote proxy pattern, do we care if the PlayerClient received information
        // from the Referee?
        public void informGameEnd(GameStatus gameStatus, PlayerResult resultForPlayer) {
            this.timeoutExceptionHandler(() -> {
                this.player.informGameEnd(gameStatus, resultForPlayer);
                return Optional.empty();
            });
            //this.player.informGameEnd(gameStatus, resultForPlayer);
        }

        void returnHome(Position homeTile) {
            this.timeoutExceptionHandler(() -> {
                this.player.returnHome(homeTile);
                return Optional.empty();
            });
        }

//        void setup(PlayerGameProjection game, Position goal) {
//            this.exceptionHandler(() -> {
//                this.player.setup(game, goal);
//                return Optional.empty();
//            });
//        }

        // Should be stored locally in the ProxyPlayer - doesn't require a network call, and thus can't time out
        public String getPlayerName() {
            return this.player.getPlayerName();
        }

        public RefereePlayerInterface getPlayerClient() {
            return this.player;
        }

        public Color getColor() {
            return this.color;
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
