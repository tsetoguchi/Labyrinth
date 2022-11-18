package game.it;

import static referee.PlayerResult.WINNER;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.model.PrivateGameState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import player.IPlayer;
import protocol.serialization.MazeJsonParser;
import referee.Referee;
import referee.clients.RefereePlayerInterface;

public class XBad2 {

  public static void executeTest() {
    try {
      MazeJsonParser mazeParser = new MazeJsonParser(System.in);
      mazeParser.readNext();
      List<IPlayer> badPlayerSpec2 = mazeParser.getPSBadPSAndBadPS2();
      mazeParser.readNext();
      PrivateGameState game = mazeParser.getGameWithGoals();

      List<IntegrationRefereePlayerInterface> intClients = new ArrayList<>();
      List<RefereePlayerInterface> clients = new ArrayList<>();
      for (IPlayer player : badPlayerSpec2) {
        IntegrationRefereePlayerInterface client = new IntegrationRefereePlayerInterface(player);
        clients.add(client);
        intClients.add(client);
      }
      Referee referee = new Referee(game, clients);
      referee.runGame();
      List<String> winnerNames = intClients.stream()
          .filter((IntegrationRefereePlayerInterface client) -> WINNER.equals(client.getResult()))
          .map(IntegrationRefereePlayerInterface::getPlayerName)
          .collect(Collectors.toList());
      Collections.sort(winnerNames);

      List<String> eliminatedNames = referee.getNamesFromAvatars();

      Object[] output = new Object[]{
          winnerNames,
          eliminatedNames
      };
      System.out.println(new ObjectMapper().writeValueAsString(output));


    } catch (Throwable e) {

    }
  }
}

