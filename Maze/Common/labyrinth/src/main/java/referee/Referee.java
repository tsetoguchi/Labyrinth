package referee;

import game.controller.ObserverController;
import game.model.*;
import game.model.projections.ObserverGameProjection;
import game.model.projections.PlayerGameProjection;
import player.TurnPlan;
import referee.clients.PlayerClient;

import java.util.*;

import static game.model.GameStatus.*;
import static referee.PlayerResult.*;

/**
 *
 * The referee handles:
 * A player asking to perform an illegal slide (TODO)
 * A player asking to move to an inaccessible position (TODO)
 * A player sending an incomplete set of moves, such as sliding and inserting but not providing a move (TODO)
 * Leaving for the remote communication layer:
 * A player not responding for a long period of time (TODO)
 */
public class Referee {
    private final Game game;
    /** Stores which client should be used to talk to each Player. **/
    private final Map<PlayerAvatar, PlayerClient> playerToClientMap;
    private final List<PlayerAvatar> playersReachedGoalList;
    private final Optional<ObserverController> observer;

    /**
     * Creates a referee and assigns it a game to moderate, as well as a list of clients which should be used to
     * interface with players. The order of the clients in the list must correspond to the order of players in the
     * Game.
     * Accepts an optional observer which will be updated as the game progresses.
     */
    public Referee(Game game, List<PlayerClient> playerClients, Optional<ObserverController> observer) {
        this.game = game;
        this.playerToClientMap = new HashMap<>();
        if (playerClients.size() != game.getPlayerList().size()) {
            throw new IllegalArgumentException("Amount of clients and players does not match.");
        }
        for (int i = 0; i < playerClients.size(); i++) {
            this.playerToClientMap.put(game.getPlayerList().get(i), playerClients.get(i));
        }
        this.playersReachedGoalList = new ArrayList<>();
        this.observer = observer;
    }

    public Referee(Game game, List<PlayerClient> playerClients) {
        this(game, playerClients, Optional.empty());
    }

    /**
     * Runs the game this referee is moderating to completion by interacting with the players via clients.
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

    /**
     * Execute one in-game turn from start to finish, kicking the active player if it misbehaves.
     */
    private void handleTurn() {
        PlayerAvatar activePlayer = this.game.getActivePlayer();
        Optional<TurnPlan> turnPlan = this.getPlanFromPlayer(activePlayer);
        if (isValidTurnPlan(turnPlan)) {
            if (turnPlan.isEmpty()) {
                this.game.skipTurn();
            }
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
        }
        else {
            this.playerToClientMap.remove(this.game.getActivePlayer());
            this.game.kickActivePlayer();
        }
    }

    private Optional<TurnPlan> getPlanFromPlayer(PlayerAvatar player) {
        PlayerClient playerClient = this.playerToClientMap.get(player);
        return playerClient.takeTurn(new PlayerGameProjection(this.game, player, this.game.getPreviousSlideAndInsert()));
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
        return expBoard.findReachableTilePositionsAfterSlideAndInsert(slideDirection, slideIndex, rotations,
                        this.game.getActivePlayer().getCurrentPosition())
                .contains(moveDestination);
    }

    private void informPlayersOfGameEnd(GameStatus gameStatus) {
        Map<PlayerAvatar, PlayerResult> playerResults = this.getResultsForPlayers();

        for (PlayerAvatar player : this.game.getPlayerList()) {
            PlayerResult resultForPlayer = playerResults.get(player);
            this.playerToClientMap.get(player).informGameEnd(gameStatus, resultForPlayer);
        }
    }

    private Map<PlayerAvatar, PlayerResult> getResultsForPlayers() {
        List<PlayerAvatar> winners = this.getWinners();

        Map<PlayerAvatar, PlayerResult> results = new HashMap<>();
        for (PlayerAvatar player : this.game.getPlayerList()) {
            if (winners.contains(player)) {
                results.put(player, WINNER);
            }
            else {
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
        }
        else {
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
     * Maps the distance of every player to their goal Tile. If the goal Tile is the spare Tile,
     * the distance is considered to be the greatest distance.
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

    private void remindPlayersReturnHome() {
        for (PlayerAvatar player : this.game.getPlayerList()) {
            if (player.hasReachedGoal() && !this.playersReachedGoalList.contains(player)) {
                this.playerToClientMap.get(player).returnHome(player.getHomePosition());
                this.playersReachedGoalList.add(player);
            }
        }
    }

    private void informObserverOfState() {
        if (this.observer.isPresent()) {
            this.observer.get().acceptState(new ObserverGameProjection(this.game));
        }
    }

    private void informObserverOfGameEnd() {
        if (this.observer.isPresent()) {
            this.observer.get().acceptGameOver();
        }
    }
}
