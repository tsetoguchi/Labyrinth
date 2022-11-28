package game.IntegrationTests.Remote;

import game.model.GameResults;
import game.model.PrivateState;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import protocol.serialization.JsonDeserializer;
import referee.Referee;
import remote.JSON.JsonUtil;
import remote.Server;

public class XServer {

  public static void main(String[] args) throws Exception {

    int port = Integer.parseInt(args[0]);
    JSONTokener jsonTokener = JsonUtil.getInput();

    JSONObject jsonGame = (JSONObject) jsonTokener.nextValue();
    PrivateState game = JsonDeserializer.jsonToState(jsonGame);

    Server server = new Server(game, port);
    GameResults results = server.call();
    System.out.println(results.resultsJson());
  }

}
