package game.IntegrationTests;

import static referee.PlayerResult.WINNER;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.model.PrivateState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import protocol.serialization.MazeJsonParser;
import referee.Referee;
import player.IPlayer;


public class XBad2 {

  public static void executeTest() {
    try {
      MazeJsonParser mazeParser = new MazeJsonParser(System.in);
      mazeParser.readNext();
      List<IPlayer> badPlayerSpec2 = mazeParser.getPSBadPSAndBadPS2();
      mazeParser.readNext();
      PrivateState game = mazeParser.getGameWithGoals();

      List<IntegrationPlayer> intClients = new ArrayList<>();
      List<IPlayer> clients = new ArrayList<>();
      for (IPlayer player : badPlayerSpec2) {
        IntegrationPlayer client = new IntegrationPlayer(player);
        clients.add(client);
        intClients.add(client);
      }
      Referee referee = new Referee(game, clients);
      referee.runGame();
      List<String> winnerNames = intClients.stream()
          .filter((IntegrationPlayer client) -> WINNER.equals(client.getResult()))
          .map(IntegrationPlayer::getPlayerName)
          .collect(Collectors.toList());
      Collections.sort(winnerNames);

      List<String> eliminatedNames = referee.getEliminatedNames();

      Object[] output = new Object[]{
          winnerNames,
          eliminatedNames
      };
      System.out.println(new ObjectMapper().writeValueAsString(output));
      System.exit(0);

    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
}

