package game.it;

import game.Controller.ObserverController;
import game.model.PrivateGameState;
import game.model.projections.ObserverGameProjection;
import protocol.serialization.MazeJsonParser;
import protocol.serialization.MazeJsonSerializer;
import referee.Referee;
import player.IPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static referee.PlayerResult.WINNER;

public class GameRefereeIntegrationTest {

    public static void executeTest(boolean includeObserver) {
        try {
            MazeJsonParser mazeParser = new MazeJsonParser(System.in);
            MazeJsonSerializer mazeSerializer = new MazeJsonSerializer();

            mazeParser.readNext();
            List<IPlayer> players = mazeParser.getPlayers();
            mazeParser.readNext();
            PrivateGameState game = mazeParser.getGameWithGoals();

            List<IntegrationRefereePlayerInterface> intClients = new ArrayList<>();
            List<IPlayer> clients = new ArrayList<>();
            for (IPlayer player : players) {
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
