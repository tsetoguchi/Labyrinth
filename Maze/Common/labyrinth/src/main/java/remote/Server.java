package remote;

import game.model.GameResults;
import game.model.PrivateState;
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

  private ExecutorService service;

  private int port = NetUtil.defaultPort;

  private final PrivateState game;
  private int numberOfWaitTimes = 2;

  public Server(PrivateState game, int port) {
    this.game = game;
    this.port = port;
  }


  /**
   * Computes a result, or throws an exception if unable to do so.
   *
   * @return computed result
   * @throws Exception if unable to compute a result
   */
  @Override
  public GameResults call() throws Exception {
    this.service = Executors.newCachedThreadPool();

    try {
      ServerSocket ss = new ServerSocket(this.port);
      System.out.println("Created server socket");
      beginSignUp(ss);
    } catch (Throwable throwable) {
    }

    if (this.proxyPlayers.size() >= 2) {

      IReferee referee = new Referee(this.proxyPlayers);
      // start game
        return referee.runGame();
    } else {
      // return empty arrays
      return new GameResults(List.of(), List.of());
    }
  }


  private void beginSignUp(ServerSocket ss) {

    for (int i = 0; i < this.numberOfWaitTimes && this.proxyPlayers.size() < 2; i++) {

      signUpPlayers();

    }

    long endTime = System.currentTimeMillis() + (NetUtil.defaultWaitPeriodSeconds * 1000);
    while (true) {
      System.out.println((endTime - System.currentTimeMillis()) / 1000);

      long waitTimeRemaining = endTime - System.currentTimeMillis();

      // 20 seconds passed and less than 2 players signed up
      if (System.currentTimeMillis() > endTime && this.proxyPlayers.size() < 2) {
        this.secondWaitPhase(ss);
        endTime = System.currentTimeMillis() + (NetUtil.defaultWaitPeriodSeconds * 1000);
        break;
      }

      // 20 seconds passed but at least 2 players signed up
      if (System.currentTimeMillis() > endTime && this.proxyPlayers.size() >= 2) {
        System.out.println("20 seconds passed and at least 2 players signed up.");
        break;
      }
      // 6 players signed up
      if (this.proxyPlayers.size() >= 6) {
        System.out.println("6 players signed up.");
        break;
      }

      this.addPlayerIfPresent(signUpClient(ss, waitTimeRemaining));
    }
  }


  private Optional<ProxyPlayerInterface> signUpClient(ServerSocket ss,
      long waitTimeRemaining) {

    System.out.println("signUpClient()");
    System.out.println(waitTimeRemaining / 1000);
    long timer = System.currentTimeMillis();
    Optional<ProxyPlayerInterface> player = null;

    // If the client failed to connect
    try {
      player = awaitSignUp(ss, waitTimeRemaining);
    } catch (IOException e) {
      return Optional.empty();
    }

    return player;
  }


  private void secondWaitPhase(ServerSocket ss) {
    long endTime = System.currentTimeMillis() + (NetUtil.defaultWaitPeriodSeconds * 1000);

    while (true) {

      // TODO:  If at most one player signs up by the end of the second waiting period,
      //  the server doesn’t run a game and instead delivers a simple default result:  [ [], [] ].

      if (System.currentTimeMillis() > endTime) {
        break;
      }

      if (this.proxyPlayers.size() >= 6) {
        break;
      }

      this.addPlayerIfPresent(this.signUpClient(ss, endTime - System.currentTimeMillis()));
    }
  }

  private void addPlayerIfPresent(Optional<ProxyPlayerInterface> proxyPlayer) {
    if (proxyPlayer.isPresent()) {
      this.proxyPlayers.add(proxyPlayer.get());
    }
  }

  private static Callable<String> awaitName(ProxyPlayerInterface player) {
    Callable<String> callableString = () -> {
      return player.getPlayerName();
    };
    return callableString;
  }

  private static Optional<ProxyPlayerInterface> awaitSignUp(ServerSocket ss, long waitTimeRemaining)
      throws IOException {

    System.out.println("awaitSignUp()");
    Callable<Socket> acceptTimeOut = () -> {
      Socket client = ss.accept();
      return client;
    };

    Socket client = null;
    try {
      client = service.submit(acceptTimeOut).get(waitTimeRemaining, TimeUnit.MILLISECONDS);
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
      ProxyPlayerInterface proxyPlayer = new ProxyPlayerInterface(finalClient, clientName);
      return proxyPlayer;
    };

    try {
      return Optional.of(service.submit(callableProxyPlayer)
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

  private void fetchPort(String[] args) {
    if (args.length > 0) {
      this.port = Integer.parseInt(args[0]);
    }
  }


}
