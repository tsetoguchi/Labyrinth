package remote;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import referee.IReferee;
import referee.Referee;

public class Server {

  private static List<ProxyPlayer> proxyPlayers = new ArrayList<>();
  private static int signUpCounter = 0;

  private static ExecutorService service;

  private static int port = NetUtil.defaultPort;


  public static void main(String[] args) throws IOException {

    // if a port number is given, set it to the server port
    fetchPort(args);

    service = Executors.newCachedThreadPool();

    try {
      ServerSocket ss = new ServerSocket(port);
      System.out.println("Created server socket");
      beginSignUp(ss);

//      IReferee referee = new Referee(proxyPlayers).runGame();

    } catch (Throwable throwable) {
      messageClients("Game failed to start.");
    }

    System.out.println("Game did not start from timeout.");
  }

  private static void beginSignUp(ServerSocket ss) throws IOException {

//    List<ProxyPlayer> proxyPlayers = new ArrayList<>();
    long endTime = System.currentTimeMillis() + 20000;
    while (true) {
      System.out.println("beginSignUp()");
      System.out.println((endTime - System.currentTimeMillis()) / 1000);

      // 20 seconds passed and less than 2 players signed up
      if (System.currentTimeMillis() > endTime && signUpCounter < 2) {
        secondWaitPhase(ss);
        break;
      }

      // 20 seconds passed but more than 2 players signed up
      if (System.currentTimeMillis() > endTime && signUpCounter > 2) {
        System.out.println("20 seconds passed but more than 2 players signed up.");
        break;
      }
      // 6 players signed up
      if (signUpCounter >= 6) {
        System.out.println("6 players signed up.");
        break;
      }

      addPlayerIfPresent(signUpClient(ss, endTime - System.currentTimeMillis()));
    }
  }


  private static Optional<ProxyPlayer> signUpClient(ServerSocket ss, long timeOut)
      throws IOException {

    System.out.println("signUpClient()");
    System.out.println(timeOut/1000);
//    Future<ProxyPlayer> signUpTimeOut = ;

    ProxyPlayer player = null;
    try {
      player = service.submit(awaitSignUp(ss)).get(2, TimeUnit.SECONDS);
    } catch (Exception throwable) {
      System.out.println(throwable.getMessage());
      System.out.println("A Player failed to sign up in time.");
      return Optional.empty();
    }

//    Future<String> nameTimeOut = service.submit(awaitName(player));
//
//    try {
//      nameTimeOut.get(2, TimeUnit.SECONDS);
//    } catch (Throwable throwable) {
//      player.getSocket().close();
//      return Optional.empty();
//    }

    return Optional.of(player);
  }


  private static void secondWaitPhase(ServerSocket ss)
      throws IOException {
    System.out.println("secondWaitPhase()");
    long endTime = System.currentTimeMillis() + 20000;

    while (true) {

      // TODO:  If at most one player signs up by the end of the second waiting period,
      //  the server doesn’t run a game and instead delivers a simple default result:  [ [], [] ].

      if (System.currentTimeMillis() > endTime) {
        break;
      }

      if (signUpCounter >= 6) {
        break;
      }

      addPlayerIfPresent(signUpClient(ss, endTime - System.currentTimeMillis()));
    }
  }

  private static void addPlayerIfPresent(Optional<ProxyPlayer> proxyPlayer) {
    if (proxyPlayer.isPresent()) {
      proxyPlayers.add(proxyPlayer.get());
    }
  }

  private static Callable<String> awaitName(ProxyPlayer player) {
    Callable<String> callableString = () -> {
      return player.getPlayerName();
    };
    return callableString;
  }

  private static Callable<ProxyPlayer> awaitSignUp(ServerSocket ss) throws IOException {
    System.out.println("awaitSignUp()");
    Socket client = ss.accept();

    System.out.println("Client connected");
    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
    messageClient(client, "Please choose a name: ", out);

    BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
    String clientName = input.readLine();


    ProxyPlayer proxyPlayer = new ProxyPlayer(client, clientName);
    Callable<ProxyPlayer> callableProxyPlayer = () -> {
      return proxyPlayer;
    };

    return callableProxyPlayer;
  }

  private static void messageClients(String message) {
    try {
      for (ProxyPlayer player : proxyPlayers) {
        player.getSocket().getOutputStream().write(message.getBytes());
      }
    } catch (Throwable throwable) {
      System.out.println("Server failed while messaging clients.");
    }
  }

  private static void messageClient(Socket client, String message, PrintWriter out) {
    try {
      out.println(message);
    } catch (Throwable throwable) {
      System.out.println("Failed to message client.");
    }
  }

//  private static <T> T timeoutHandler(Future<T> future, long timeout, String exceptionMessage) {
//    try {
//      return future.get(timeout, TimeUnit.SECONDS);
//    } catch (Throwable throwable) {
//      System.out.println(exceptionMessage);
//    }
//
//  }

  private static void fetchPort(String[] args) {
    if (args.length > 0) {
      port = Integer.parseInt(args[0]);
    }
  }


}
