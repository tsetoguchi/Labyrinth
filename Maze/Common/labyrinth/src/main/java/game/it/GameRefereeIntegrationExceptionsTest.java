package game.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.model.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import player.Player;
import protocol.serialization.MazeJsonParser;
import protocol.serialization.MazeJsonSerializer;
import referee.Referee;
import referee.clients.PlayerClient;

import static referee.PlayerResult.WINNER;

public class GameRefereeIntegrationExceptionsTest {

  private static final int BOARD_SIZE = 7;

  public static void executeTest() {

    try {
      MazeJsonParser mazeParser = new MazeJsonParser(System.in, BOARD_SIZE, BOARD_SIZE);

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
      List<String> winnerNames = intClients.stream()
          .filter((IntegrationPlayerClient client) -> WINNER.equals(client.getResult()))
          .map(IntegrationPlayerClient::getPlayerName)
          .collect(Collectors.toList());
      Collections.sort(winnerNames);

      List<String> eliminatedNames = referee.getEliminated();

      Object[] output = new Object[]{
          winnerNames,
          eliminatedNames
      };
      System.out.println(new ObjectMapper().writeValueAsString(output));

    } catch (Throwable throwable) {
      throw new RuntimeException(throwable);
    }

  }

}
