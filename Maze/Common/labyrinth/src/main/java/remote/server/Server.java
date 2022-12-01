package remote.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import model.Position;
import model.state.GameResults;
import model.state.IState;
import player.IPlayer;
import referee.IReferee;
import referee.Referee;

public class Server implements Callable<GameResults> {

  public static final int WAIT_PERIOD_SECONDS = 20;
  public static final int PLAYER_SIGN_UP_SECONDS = 2;
  public static final int NUMBER_OF_WAIT_TIMES = 2;
  private static final int MAX_NUMBER_OF_PLAYERS = 6;
  private static final int MIN_NUMBER_OF_PLAYERS = 2;

  private final IState game;
  private final List<Position> goals;
  private final List<IPlayer> players;

  private final ServerSocket serverSocket;
  private final ExecutorService service;

  public Server(IState game, List<Position> goals, int port) {
    this.game = game;
    this.goals = goals;
    this.players = new ArrayList<>();
    this.service = Executors.newCachedThreadPool();

    try{
      this.serverSocket = new ServerSocket(port);
    } catch (IOException e){
      throw new RuntimeException(e);
    }

  }


  /**
   * Signs up players and runs a game to completion and returns the results of the game.
   * @return computed result
   */
  @Override
  public GameResults call() {

    try {
      this.beginSignUp();
    } catch (Throwable ignore) {}

    if (this.players.size() >= MIN_NUMBER_OF_PLAYERS) {
      IReferee referee = new Referee(this.game, this.players, this.goals);
      return referee.runGame();
    } else {
      return new GameResults(List.of(), List.of());
    }
  }


  private void beginSignUp() {
    for (int i = 0; i < NUMBER_OF_WAIT_TIMES && this.players.size() < MIN_NUMBER_OF_PLAYERS;
         i++) {
      this.signUpPlayers();
    }
  }

  private void signUpPlayers() {
    long endTime = System.currentTimeMillis() + (WAIT_PERIOD_SECONDS * 1000);
    while (System.currentTimeMillis() < endTime && this.players.size() >= MAX_NUMBER_OF_PLAYERS) {

      long waitTimeRemaining = endTime - System.currentTimeMillis();

      this.signUpClient(waitTimeRemaining);
    }
  }


  private void signUpClient(long waitTimeRemaining) {

    Optional<ProxyPlayer> player;
    try {
      player = this.awaitSignUp(waitTimeRemaining);
      if (player.isPresent()) {
        this.players.add(player.get());
      }
    } catch (IOException ignore) {}

  }


  private Optional<ProxyPlayer> awaitSignUp(long waitTimeRemaining) throws IOException {

    Callable<Socket> acceptConnection = () -> {
      return this.serverSocket.accept();
    };

    Socket client;
    try {
      client = this.service.submit(acceptConnection).get(waitTimeRemaining, TimeUnit.MILLISECONDS);
    } catch (Throwable e) {
      return Optional.empty();
    }

    BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

    Callable<ProxyPlayer> callableProxyPlayer = () -> {
      String clientName = input.readLine();
      return new ProxyPlayer(client, clientName);
    };

    try {
      return Optional.of(this.service.submit(callableProxyPlayer)
              .get(PLAYER_SIGN_UP_SECONDS, TimeUnit.SECONDS));
    } catch (Throwable throwable) {
      client.close();
      return Optional.empty();
    }
  }


}
