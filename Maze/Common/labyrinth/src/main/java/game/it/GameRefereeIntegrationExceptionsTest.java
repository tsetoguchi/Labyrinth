package game.it;

import game.model.Game;
import java.util.ArrayList;
import java.util.List;
import player.Player;
import protocol.serialization.MazeJsonParser;
import protocol.serialization.MazeJsonSerializer;
import referee.Referee;
import referee.clients.PlayerClient;

public class GameRefereeIntegrationExceptionsTest {

  private static final int BOARD_SIZE = 7;

  public static void executeTest() {

    try {
      MazeJsonParser mazeParser = new MazeJsonParser(System.in, BOARD_SIZE, BOARD_SIZE);
      MazeJsonSerializer mazeSerializer = new MazeJsonSerializer();

      mazeParser.readNext();
      List<Player> badPlayerSpec = mazeParser.getPSAndBadPS();

      mazeParser.readNext();
      Game game = mazeParser.getGameWithGoals();

      List<IntegrationPlayerClient> intClients = new ArrayList<>();
      List<PlayerClient> clients = new ArrayList<>();
      for (Player player : badPlayerSpec) {
        IntegrationPlayerClient client = new IntegrationPlayerClient(player);
        clients.add(client);
        intClients.add(client);
      }

      Referee referee = new Referee(game, clients);
      referee.runGame();


    } catch (Throwable throwable) {

    }

  }

}
