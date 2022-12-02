package IntegrationTests.Remote;

import IntegrationTests.IntegrationUtils;
import IntegrationTests.TestPlayer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import player.IPlayer;
import remote.client.Client;


/**
 *
 */
public class XClients {

  public final static int CLIENT_WAIT_TIMER = 3;

  public static void main(String[] args)
      throws JSONException, UnknownHostException, InterruptedException {

    int port = Integer.parseInt(args[0]);
    JSONTokener jsonTokener = IntegrationUtils.getInput();

    InetAddress inetAddress = InetAddress.getLocalHost();
    if (args.length > 1) {
      inetAddress = InetAddress.getByName(args[1]);
    }


    JSONArray playersJSON = (JSONArray) jsonTokener.nextValue();
    List<IPlayer> playerSpec = TestPlayer.jsonToTestPlayers(playersJSON);

    List<Client> clients = new ArrayList<>();
    for (IPlayer player : playerSpec) {
      clients.add(new Client(player, inetAddress, port));
    }

    List<Thread> clientThreads = runClients(clients);


  }

  private static List<Thread> runClients(List<Client> clients) throws InterruptedException {

    List<Thread> threads = new ArrayList<>();

    for (int i = 0; i < clients.size(); i++) {
      Thread current = new Thread(clients.get(i), Integer.toString(i));
      current.start();
      TimeUnit.SECONDS.sleep(1);
      threads.add(current);
    }
    return threads;
  }


}
