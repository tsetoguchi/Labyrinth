//package IntegrationTests;
//
//import observer.Controller.ObserverController;
//import model.state.IState;
//import protocol.serialization.JsonSerializer;
//
//import referee.Referee;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//import player.IPlayer;
//
//
//import static referee.PlayerResult.WINNER;
//
//public class GameRefereeIntegrationTest {
//
//    public static void executeTest(boolean includeObserver) {
//        try {
//            MazeJsonParser mazeParser = new MazeJsonParser(System.in);
//            JsonSerializer mazeSerializer = new JsonSerializer();
//
//            mazeParser.readNext();
//            List<IPlayer> players = mazeParser.getPlayers();
//            mazeParser.readNext();
//            IState game = mazeParser.getGameWithGoals();
//
//            List<IntegrationPlayer> intClients = new ArrayList<>();
//            List<IPlayer> clients = new ArrayList<>();
//            for (IPlayer player : players) {
//                IntegrationPlayer client = new IntegrationPlayer(player);
//                clients.add(client);
//                intClients.add(client);
//            }
//
//            Referee referee = null;
//            if (includeObserver) {
//                ObserverController observer = new ObserverController(new ObserverGameProjection(game)); //todo change
//                referee = new Referee(game, clients, List.of(observer));
//            }
//            else {
//                referee = new Referee(game, clients);
//            }
//            referee.runGame();
//
//            List<String> winnerNames = intClients.stream()
//                    .filter((IntegrationPlayer client) -> WINNER.equals(client.getResult()))
//                    .map(IntegrationPlayer::getPlayerName)
//                    .collect(Collectors.toList());
//
//            System.out.println(mazeSerializer.namesToJson(winnerNames));
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
