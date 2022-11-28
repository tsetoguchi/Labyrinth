package game.IntegrationTests.Remote;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;
import player.IPlayer;
import protocol.serialization.JsonDeserializer;
import remote.JSON.JsonUtil;
import remote.client.PlayerClient;


/**
 *
 */
public class XClients {

  public final static int clientWaitTimer = 3;

  public static void main(String[] args)
      throws JSONException, UnknownHostException, InterruptedException {

    int port = Integer.parseInt(args[0]);

    InetAddress inetAddress = InetAddress.getLocalHost();
    if (args.length > 1) {
      inetAddress = InetAddress.getByName(args[1]);
    }

    JSONTokener jsonTokener = JsonUtil.getInput();
    JSONArray playersSpecJson = (JSONArray) jsonTokener.nextValue();
    List<IPlayer> playerSpec = JsonDeserializer.jsonToPlayerSpec(playersSpecJson);

    List<PlayerClient> clients = new ArrayList<>();
    for (IPlayer player : playerSpec) {
      clients.add(new PlayerClient(player, inetAddress, port));
    }

    List<Thread> clientThreads = runClients(clients);

  }

  private static List<Thread> runClients(List<PlayerClient> clients) throws InterruptedException {

    List<Thread> threads = new ArrayList<>();

    for (int i = 0; i < clients.size(); i++) {
      Thread current = new Thread(clients.get(i), Integer.toString(i));
      current.wait((long) clientWaitTimer * 1000 * i);
      current.start();
      threads.add(current);
    }
    return threads;
  }


}
