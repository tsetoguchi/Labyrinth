package IntegrationTests;

import static referee.PlayerResult.WINNER;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import model.state.IState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import protocol.serialization.JsonDeserializer;
import referee.Referee;
import player.IPlayer;
import remote.JSON.JsonUtil;


public class XBad2 {

  public static void executeTest() throws JSONException {

    JSONTokener jsonTokener = JsonUtil.getInput();

    JSONArray playersJSON = (JSONArray) jsonTokener.nextValue();
    List<IPlayer> players = XBad2.jsonToBadPlayers(playersJSON);

    JSONObject stateJSON = (JSONObject) jsonTokener.nextValue();
    IState game = JsonDeserializer.jsonToState(stateJSON);

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

  }
}

