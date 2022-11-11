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
import referee.clients.PlayerClient;

import java.util.*;

import static game.model.GameStatus.*;
import static referee.PlayerResult.*;

/**
 * The referee handles: A player asking to perform an illegal slide (TODO) A player asking to move
 * to an inaccessible position (TODO) A player sending an incomplete set of moves, such as sliding
 * and inserting but not providing a move (TODO) Leaving for the remote communication layer: A
 * player not responding for a long period of time (TODO)
 */
public class Referee implements IReferee {

    private Game game;
    /**
     * Stores which client should be used to talk to each Player.
     **/
    private Map<PlayerAvatar, PlayerClient> playerAvatarToClient;
    private final Map<PlayerAvatar, PlayerHandler> playerAvatarToHandler;
    private final List<PlayerAvatar> playersCollectedTreasures;
    private final List<IObserver> observers;

    // the eliminated players so far, including cheaters
    private final List<PlayerClient> eliminated;

    private static final int TIMEOUT = 15;

    /**
     * Creates a referee and assigns it a game to moderate, as well as a list of clients which should
     * be used to interface with players. The order of the clients in the list must correspond to the
     * order of players in the Game. Accepts an optional observer which will be updated as the game
     * progresses.
     */
    public Referee(Game game, List<PlayerClient> playerClients, List<IObserver> observers) {
        this.game = game;
        this.playerAvatarToClient = this.mapPlayerAvatarsToPlayerClients(game, playerClients);
        this.playerAvatarToHandler = this.mapPlayerAvatarsToPlayerHandlers(game, playerClients);
        this.playersCollectedTreasures = new ArrayList<>();
        this.observers = observers;
        this.eliminated = new ArrayList<>();
    }

    public Referee(Game game, List<PlayerClient> playerClients) {
        this(game, playerClients, List.of());
    }

    /**
     * Runs the game this referee is moderating to completion by interacting with the players via
     * clients.
     */
    public void runGame() {
        this.informObserverOfState();

        GameStatus gameStatus = this.game.getGameStatus();
        while (gameStatus == IN_PROGRESS) {
            this.handleTurn();
            gameStatus = this.game.getGameStatus();
        }
        this.informPlayersOfGameEnd(gameStatus);
        this.informObserverOfGameEnd();
    }

    public void resume(Game game, List<PlayerClient> playerClients) {
        this.playerAvatarToClient = mapPlayerAvatarsToPlayerClients(game, playerClients);
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
            return;
        }
        Optional<TurnPlan> turnPlan = turnWrapper.getTurnPlan();
        if (isValidTurnPlan(turnPlan)) {

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

            // TODO: Clean this up
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

        if (!game.isValidSlideAndInsert(slideDirection, slideIndex, rotations)) {
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

//            this.playerAvatarToClient.get(player).informGameEnd(gameStatus, resultForPlayer);
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

    public List<String> getEliminated() {
        List<String> names = new ArrayList<>();
        for (PlayerClient player : this.eliminated) {
            names.add(player.getPlayerName());
        }
        Collections.sort(names);
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

    private Map<PlayerAvatar, PlayerClient> mapPlayerAvatarsToPlayerClients(Game game, List<PlayerClient> playerClients) {
        Map<PlayerAvatar, PlayerClient> map = new HashMap<>();
        if (playerClients.size() != game.getPlayerList().size()) {
            throw new IllegalArgumentException("Amount of clients and players do not match.");
        }
        for (int i = 0; i < playerClients.size(); i++) {
            map.put(game.getPlayerList().get(i), playerClients.get(i));
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
        for (PlayerAvatar player : this.game.getPlayerList()) {

        }
    }

    private void informObserverOfState() {
        for (IObserver observer : this.observers) {
            observer.update(new ObserverGameProjection(this.game));
        }
    }

    private void informObserverOfGameEnd() {

        for (IObserver observer : this.observers) {
            observer.gameOver();
        }
    }

    private Map<PlayerAvatar, PlayerHandler> mapPlayerAvatarsToPlayerHandlers(Game game, List<PlayerClient> playerClients) {
        if (playerClients.size() != game.getPlayerList().size()) {
            throw new IllegalArgumentException("Amount of clients and players do not match.");
        }

        Map<PlayerAvatar, PlayerHandler> playersToHandlers = new HashMap<>();

        for (int i = 0; i < game.getPlayerList().size(); i++) {
            PlayerAvatar playerAvatar = game.getPlayerList().get(i);
            PlayerHandler playerHandler = new PlayerHandler(playerAvatar.getColor(), playerClients.get(i));
            playersToHandlers.put(playerAvatar, playerHandler);
        }
        return playersToHandlers;
    }

    /**
     * Removes the player with the specified color from the playerToClientMap and game
     */
    private void kickPlayerInGameAndRef(Color color) {

        Optional<PlayerAvatar> playerAvatar = this.game.getPlayer(color);

        if (playerAvatar.isPresent()) {
            this.eliminated.add(this.playerAvatarToHandler.get(playerAvatar.get()).getPlayerClient());
        }

        this.kickPlayer(color);
        this.game.kickPlayer(color);
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
     * Wrapper for the PlayerClient class, adding exception and timeout handling for any PlayerClient calls.
     */
    private class PlayerHandler {

        private final Color color;
        private final PlayerClient player;

        public PlayerHandler(Color color, PlayerClient player) {
            this.color = color;
            this.player = player;
        }

        private <T> Optional<T> exceptionHandler(Supplier<T> function) {
            try {
                ExecutorService service = Executors.newCachedThreadPool();
                return Optional.of(service.submit(function::get).get(TIMEOUT, TimeUnit.SECONDS));
            } catch (Throwable exception) {
                kickPlayerInGameAndRef(this.color);
                return Optional.empty();
            }
        }

        public void win(boolean w) {
            this.exceptionHandler(() -> {
                this.player.win(w);
                return Optional.empty();
            });
        }

        public Optional<Optional<TurnPlan>> takeTurn(PlayerGameProjection game) {
            return this.exceptionHandler(() -> {
                return this.player.takeTurn(game);
            });
        }

        public Optional<Boolean> setup(PlayerGameProjection state, Position goal) {
            return this.exceptionHandler(() -> {
                return this.player.setup(state, goal);
            });
        }

        // TODO: Understand more about remote proxy pattern, do we care if the PlayerClient received information
        // from the Referee?
        public void informGameEnd(GameStatus gameStatus, PlayerResult resultForPlayer) {
            this.exceptionHandler(() -> {
                this.player.informGameEnd(gameStatus, resultForPlayer);
                return Optional.empty();
            });
            //this.player.informGameEnd(gameStatus, resultForPlayer);
        }

        void returnHome(Position homeTile) {
            this.exceptionHandler(() -> {
                this.player.returnHome(homeTile);
                return Optional.empty();
            });
        }

        void

        public Optional<String> getPlayerName() {
            return this.exceptionHandler(() -> {
                return this.player.getPlayerName();
            });
        }

        public PlayerClient getPlayerClient() {
            return this.player;
        }

    }
}
