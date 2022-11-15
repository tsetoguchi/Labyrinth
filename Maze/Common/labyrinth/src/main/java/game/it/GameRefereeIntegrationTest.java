package game.it;

import game.controller.ObserverController;
import game.model.PrivateGameState;
import game.model.projections.ObserverGameProjection;
import protocol.serialization.MazeJsonParser;
import protocol.serialization.MazeJsonSerializer;
import player.Player;
import referee.Referee;
import referee.clients.RefereePlayerInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static referee.PlayerResult.WINNER;

public class GameRefereeIntegrationTest {
    private static final int BOARD_SIZE = 7;

    public static void executeTest(boolean includeObserver) {
        try {
            MazeJsonParser mazeParser = new MazeJsonParser(System.in, BOARD_SIZE, BOARD_SIZE);
            MazeJsonSerializer mazeSerializer = new MazeJsonSerializer();

            mazeParser.readNext();
            List<Player> players = mazeParser.getPlayers();
            mazeParser.readNext();
            PrivateGameState game = mazeParser.getGameWithGoals();

            List<IntegrationRefereePlayerInterface> intClients = new ArrayList<>();
            List<RefereePlayerInterface> clients = new ArrayList<>();
            for (Player player : players) {
                IntegrationRefereePlayerInterface client = new IntegrationRefereePlayerInterface(player);
                clients.add(client);
                intClients.add(client);
            }

            Referee referee = null;
            if (includeObserver) {
                ObserverController observer = new ObserverController(new ObserverGameProjection(game)); //todo change
                referee = new Referee(game, clients, List.of(observer));
            }
            else {
                referee = new Referee(game, clients);
            }
            referee.runGame();

            List<String> winnerNames = intClients.stream()
                    .filter((IntegrationRefereePlayerInterface client) -> WINNER.equals(client.getResult()))
                    .map(IntegrationRefereePlayerInterface::getPlayerName)
                    .collect(Collectors.toList());

            System.out.println(mazeSerializer.namesToJson(winnerNames));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
