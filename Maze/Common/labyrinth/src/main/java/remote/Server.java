package remote;

import model.state.GameResults;
import model.state.IState;
import referee.IReferee;
import referee.Referee;
import player.IPlayer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

public class Server implements Callable<GameResults> {

  private final List<IPlayer> proxyPlayers = new ArrayList<>();
  private final int port;
  private final IState game;
  private ExecutorService service;

  private static final int minNumberOfPlayers = 2;

  public Server(IState game, int port) {
    this.game = game;
    this.port = port;
  }


  /**
   * Computes a result, or throws an exception if unable to do so.
   *
   * @return computed result
   */
  @Override
  public GameResults call() {
    this.service = Executors.newCachedThreadPool();

    try {
      ServerSocket ss = new ServerSocket(this.port);
      System.out.println("Created server socket");
      this.beginSignUp(ss);
    } catch (Throwable throwable) {

    }

    if (this.proxyPlayers.size() >= minNumberOfPlayers) {

      IReferee referee = new Referee(this.game, this.proxyPlayers);
      return referee.runGame();
    } else {
      return new GameResults(List.of(), List.of());
    }
  }


  private void beginSignUp(ServerSocket ss) {

    for (int i = 0; i < NetUtil.numberOfWaitTimes && this.proxyPlayers.size() < minNumberOfPlayers;
         i++) {

      this.signUpPlayers(ss);

    }


  }

  private void signUpPlayers(ServerSocket ss) {
    long endTime = System.currentTimeMillis() + (NetUtil.defaultWaitPeriodSeconds * 1000);
    while (true) {

      // if the current time has exceeded the sign up period, leave the while loop
      if (System.currentTimeMillis() > endTime) {
        break;
      }

      long waitTimeRemaining = endTime - System.currentTimeMillis();

      // 6 players signed up
      if (this.proxyPlayers.size() >= 6) {
        break;
      }

      this.addPlayerIfPresent(this.signUpClient(ss, waitTimeRemaining));
    }
  }


  private Optional<ProxyPlayerInterface> signUpClient(ServerSocket ss,
                                                      long waitTimeRemaining) {

    System.out.println("signUpClient()");
    Optional<ProxyPlayerInterface> player;

    // If the client failed to connect
    try {
      player = this.awaitSignUp(ss, waitTimeRemaining);
    } catch (IOException e) {
      return Optional.empty();
    }

    return player;
  }


  private void addPlayerIfPresent(Optional<ProxyPlayerInterface> proxyPlayer) {
    if (proxyPlayer.isPresent()) {
      this.proxyPlayers.add(proxyPlayer.get());
    }
  }


  private Optional<ProxyPlayerInterface> awaitSignUp(ServerSocket ss, long waitTimeRemaining)
          throws IOException {

    System.out.println("awaitSignUp()");
    Callable<Socket> acceptTimeOut = () -> {
      Socket client = ss.accept();
      return client;
    };

    Socket client = null;
    try {
      client = this.service.submit(acceptTimeOut).get(waitTimeRemaining, TimeUnit.MILLISECONDS);
    } catch (Throwable e) {
      System.out.println("Accept timed out");
      return Optional.empty();
    }

    System.out.println("Client connected");
    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
    messageClient("Please choose a name: ", out);

    BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

    Socket finalClient = client;

    Callable<ProxyPlayerInterface> callableProxyPlayer = () -> {
      String clientName = input.readLine();
      return new ProxyPlayerInterface(finalClient, clientName);
    };

    try {
      return Optional.of(this.service.submit(callableProxyPlayer)
              .get(NetUtil.defaultPlayerSignUpSeconds, TimeUnit.SECONDS));
    } catch (Throwable throwable) {
      messageClient("close", out);
      client.close();
      return Optional.empty();
    }
  }

  private static void messageClient(String message, PrintWriter out) {
    try {
      out.println(message);
    } catch (Throwable throwable) {
      System.out.println("Failed to message client.");
    }
  }


}
