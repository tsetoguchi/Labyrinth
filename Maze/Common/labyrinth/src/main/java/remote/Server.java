package remote;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Server {

  private static List<ProxyPlayer> proxyPlayers = new ArrayList<>();
  private static int signUpCounter = 0;

  private static ExecutorService service;


  public static void main(String[] args) {

    // if we need it
    int port = Integer.parseInt(args[0]);

    service = Executors.newCachedThreadPool();

    try (ServerSocket ss = new ServerSocket(NetUtil.defaultPort)) {

//      Future<List<ProxyPlayer>> proxyPlayers = service.submit(beginSignUp(ss));
//      proxyPlayers.get(40, TimeUnit.SECONDS);

      beginSignUp(ss);

    } catch (Exception e) {

    }
  }


  private static Optional<ProxyPlayer> signUpClient(ServerSocket ss, long timeOut)
      throws IOException {

    Future<ProxyPlayer> signUpTimeOut = service.submit(awaitSignUp(ss));

    ProxyPlayer player = null;
    try {
      player = signUpTimeOut.get(timeOut, TimeUnit.MILLISECONDS);
    } catch (Throwable throwable) {
      return Optional.empty();
    }

    Future<String> nameTimeOut = service.submit(awaitName(player));

    try {
      nameTimeOut.get(2, TimeUnit.SECONDS);
    } catch (Throwable throwable) {
      player.getSocket().close();
      return Optional.empty();
    }

    return Optional.of(player);
  }


  private static void beginSignUp(ServerSocket ss) throws IOException {

//    List<ProxyPlayer> proxyPlayers = new ArrayList<>();
    long endTime = System.currentTimeMillis() + 20000;
    while (true) {

      // 20 seconds passed and less than 2 players signed up
      if (System.currentTimeMillis() > endTime && signUpCounter < 2) {
        secondWaitPhase(ss, proxyPlayers);
        break;
      }

      // 20 seconds passed but more than 2 players signed up
      if (System.currentTimeMillis() > endTime && signUpCounter > 2) {
        break;
      }

      // 6 players signed up
      if (signUpCounter >= 6) {
        break;
      }

      Optional<ProxyPlayer> proxyPlayer = signUpClient(ss, endTime - System.currentTimeMillis());
      if (proxyPlayer.isPresent()) {
        proxyPlayers.add(proxyPlayer.get());
      }
    }
  }


  private static void secondWaitPhase(ServerSocket ss, List<ProxyPlayer> proxyPlayers)
      throws IOException {
    long endTime = System.currentTimeMillis() + 20000;

    while (true) {
      if (System.currentTimeMillis() > endTime) {
        break;
      }
      if (signUpCounter >= 6) {
        break;
      }
      ProxyPlayer pp = signUpClient(ss, );
      proxyPlayers.add(pp);
    }
  }

  private static Callable<String> awaitName(ProxyPlayer player) {
    Callable<String> callableString = () -> {
      return player.getPlayerName();
    };
    return callableString;
  }

  private static Callable<ProxyPlayer> awaitSignUp(ServerSocket ss) throws IOException {
    Socket client = ss.accept();
    ProxyPlayer proxyPlayer = new ProxyPlayer(client);
    Callable<ProxyPlayer> callableProxyPlayer = () -> {
      return proxyPlayer;
    };

    return callableProxyPlayer;
  }


}
