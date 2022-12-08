package remote.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
  private final List<Socket> connections;

  private final ServerSocket serverSocket;
  private final ExecutorService service;

  public Server(IState game, List<Position> goals, int port) {
    this.game = game;
    this.goals = goals;
    this.players = new ArrayList<>();
    this.connections = new ArrayList<>();
    this.service = Executors.newCachedThreadPool();

    try{
      this.serverSocket = new ServerSocket(port);
    } catch (IOException e){
      throw new RuntimeException(e);
    }
  }


  /**
   * Signs up players and runs a game to completion and returns the results of the game.
   */
  @Override
  public GameResults call() {

    try {
      this.beginSignUp();
    } catch (Throwable ignore) {}

    if (this.players.size() >= MIN_NUMBER_OF_PLAYERS) {

      // Reverse the player list so that the youngest player is in the front
      Collections.reverse(this.players);

      IReferee referee = new Referee(this.game, this.players, this.goals);
      return referee.runGame();
    } else {
      return new GameResults(List.of(), List.of());
    }
  }


  /**
   * Signs up clients until the specified number of wait times or enough players have joined
   */
  private void beginSignUp() {

    for (int i = 0; i < NUMBER_OF_WAIT_TIMES && this.connections.size() < MIN_NUMBER_OF_PLAYERS;
         i++) {
      long endTime = System.currentTimeMillis() + (WAIT_PERIOD_SECONDS * 1000);

      while (System.currentTimeMillis() < endTime && this.connections.size() < MAX_NUMBER_OF_PLAYERS) {
        long waitTimeRemaining = endTime - System.currentTimeMillis();
        this.connections.add(this.signUpClient(waitTimeRemaining));
      }
    }

    this.createPlayers();

  }

  /**
   * Creates the players form the connections by receiving their names.
   */
  private void createPlayers() {
    for(Socket s : this.connections){
      Callable<ProxyPlayer> makePlayer = () -> {
        DataInputStream in = new DataInputStream(s.getInputStream());
        String name = in.readUTF();
        System.out.println(name);
        return new ProxyPlayer(s, name);
      };

      try {
        Future<ProxyPlayer> futurePlayer = this.service.submit(makePlayer);
        this.players.add(futurePlayer.get(PLAYER_SIGN_UP_SECONDS, TimeUnit.MILLISECONDS));
      }catch(Throwable t) {
        try{
          s.close();
        } catch(Throwable ignore){}
      }
    }
  }

  /**
   * Signs up the current client if the connection was successful.
   * The waitTimeRemaining is the amount of time left until the waiting period is over.
   */
  private Socket signUpClient(long waitTimeRemaining) {
    Callable<Socket> acceptConnection = () -> {
      return this.serverSocket.accept();
    };

    try {
      Future<Socket> futurePlayer = this.service.submit(acceptConnection);
      return futurePlayer.get(waitTimeRemaining, TimeUnit.MILLISECONDS);
    }catch(Exception ignore){}
    return null;
  }

}
