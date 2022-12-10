package remote;

import static model.board.Direction.DOWN;
import static model.board.Direction.LEFT;
import static model.board.Direction.RIGHT;
import static model.board.Direction.UP;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import json.JsonSerializer;
import model.Position;
import model.board.Board;
import model.board.Direction;
import model.board.Gem;
import model.board.IBoard;
import model.board.Tile;
import model.board.Treasure;
import model.state.IState;
import model.state.PlayerAvatar;
import model.state.State;
import model.state.StateProjection;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;
import player.EuclideanStrategy;
import player.IPlayer;
import player.Player;
import remote.client.ProxyReferee;
import remote.server.ProxyPlayer;

public class ProxyRefereeTest {

  private ProxyReferee proxyRef;
  private IState state;


  public void setup() {
    Tile[][] tiles = new Tile[7][7];

    Set<Direction> dir = Set.of(UP, RIGHT, DOWN, LEFT);

    for (int i = 0; i < 7; i++) {
      for (int j = 0; j < 7; j++) {
        tiles[i][j] = new Tile(dir, new Treasure(Gem.zircon, Gem.zircon));
      }
    }

    Tile initialSpare = new Tile(dir, new Treasure(Gem.zircon, Gem.zircon));
    IBoard board = new Board(7, 7, tiles, initialSpare);

    PlayerAvatar p1 = new PlayerAvatar(Color.RED, new Position(1, 1));
    PlayerAvatar p2 = new PlayerAvatar(Color.BLUE, new Position(3, 3));

    this.state = new State(board, Arrays.asList(p1, p2));
  }


//  @Test
//  public void testNetUtilsReadInput(){
//    StringBuilder str = new StringBuilder();
//    TestSocket sock = new TestSocket();
//
//    sock.addInput("hello");
//    NetUtil.readInput(str, sock);
//    assertEquals(str.toString(), "hello");
//
//  }

  @Test
  public void goodData() throws IOException {
    ServerSocket serverSocket = new ServerSocket(11111);
    Socket clientSock = new Socket(InetAddress.getLocalHost(),11111);
    Socket serverSock = serverSocket.accept();

    IPlayer player = new Player("joe", new EuclideanStrategy());
    ProxyReferee pf = new ProxyReferee(clientSock, player);
    Thread t = new Thread(pf, "proxyReferee");
    t.start();

    //Read in name:
    NetUtil.readInput(new StringBuilder(), serverSock);



    ProxyPlayer pp = new ProxyPlayer(serverSock, "joe");

    assertTrue(pp.win(true));
  }

  @Test
  public void messyData() throws IOException, JSONException {
    ServerSocket serverSocket = new ServerSocket(12345);
    Socket clientSock = new Socket(InetAddress.getLocalHost(),12345);
    Socket serverSock = serverSocket.accept();

    IPlayer player = new Player("joe", new EuclideanStrategy());
    ProxyReferee pf = new ProxyReferee(clientSock, player);
    Thread t = new Thread(pf, "proxyReferee");
    t.start();

    NetUtil.readInput(new StringBuilder(), serverSock);

    BadProxyPlayer pp = new BadProxyPlayer(serverSock, "joe");

    pp.setAfterNoise("hello world");

    Boolean response = pp.setup(Optional.empty(), new Position(3, 3));

    assertTrue(response);

    pp.setBeforeNoise("hello world");

    Optional<Boolean> potentialEnd = this.timeoutExceptionHandler(() -> {
      return pp.win(true);
    });

    assertTrue(potentialEnd.isEmpty());

  }

  private <T> Optional<T> timeoutExceptionHandler(Supplier<T> function) {
    try {
      ExecutorService service = Executors.newCachedThreadPool();
      return Optional.of(service.submit(function::get).get(5, TimeUnit.SECONDS));
    } catch (Throwable exception) {
      return Optional.empty();
    }
  }



  public class BadProxyPlayer extends ProxyPlayer {

    String beforeNoise;
    String afterNoise;

    public BadProxyPlayer(Socket socket, String playerName) {
      super(socket, playerName);
      this.beforeNoise = "";
      this.afterNoise = "";
    }

    public void setBeforeNoise(String str){
      beforeNoise = str;
    }

    public void setAfterNoise(String str){
      afterNoise = str;
    }



    @Override
    public boolean setup(Optional<StateProjection> game, Position goal) {

      try {
        JSONArray toSend = JsonSerializer.setup(game, goal);
        NetUtil.sendOutput(this.beforeNoise + toSend.toString() + this.afterNoise, this.socket);

      } catch (JSONException e) {
        e.printStackTrace();
      }

      StringBuilder response = new StringBuilder();
      while (true) {
        NetUtil.readInput(response, this.socket);

        if (response.toString().equals("\"void\"")) {
          return true;
        }

      }
    }

    @Override
    public boolean win(boolean won){
      JSONArray toSend = JsonSerializer.win(won);
      NetUtil.sendOutput(this.beforeNoise + toSend.toString() + this.afterNoise, this.socket);

      StringBuilder response = new StringBuilder();
      while (true) {
        NetUtil.readInput(response, this.socket);

        if (response.toString().equals("\"void\"")) {
          return true;
        }

      }
    }


  }
}
