package java.IntegrationTests.Remote;

import IntegrationTests.IntegrationUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

import json.JsonDeserializer;
import json.JsonSerializer;
import model.Position;
import model.state.GameResults;
import model.state.IState;
import remote.server.Server;

public class XServer {

  public static void main(String[] args) throws Exception {

    int port = Integer.parseInt(args[0]);
    JSONTokener jsonTokener = IntegrationUtils.getInput();

    JSONObject jsonGame = (JSONObject) jsonTokener.nextValue();
    IState game = JsonDeserializer.state(jsonGame);

    JSONArray jsonPlmt = jsonGame.getJSONArray("plmt");
    List<Position> goals = JsonDeserializer.jsonToGoals(jsonPlmt);

    Server server = new Server(game, goals, port);
    GameResults results = server.call();
    System.out.println(JsonSerializer.gameResults(results));
  }

}
