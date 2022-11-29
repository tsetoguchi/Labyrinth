package IntegrationTests.Remote;

import model.state.GameResults;
import model.state.IState;

import org.json.JSONObject;
import org.json.JSONTokener;
import protocol.serialization.JsonDeserializer;
import remote.JSON.JsonUtil;
import remote.Server;

public class XServer {

  public static void main(String[] args) throws Exception {

    int port = Integer.parseInt(args[0]);
    JSONTokener jsonTokener = JsonUtil.getInput();

    JSONObject jsonGame = (JSONObject) jsonTokener.nextValue();
    IState game = JsonDeserializer.jsonToState(jsonGame);

    Server server = new Server(game, port);
    GameResults results = server.call();
    System.out.println(results.resultsJson());
  }

}
