package remote;

import game.exceptions.IllegalGameActionException;
import game.exceptions.IllegalPlayerActionException;
import game.model.*;
import game.model.projections.PlayerGameProjection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import player.TurnPlan;
import protocol.serialization.MazeJsonParser;
import protocol.serialization.MazeJsonSerializer;
import referee.PlayerResult;
import referee.clients.RefereePlayerInterface;
import remote.json.MethodJsonSerializer;

import java.net.Socket;
import java.util.Optional;

/**
 * The communication interface for a referee to communicate with a player through a network. The
 * functions will be responsible for taking in a model data representation, serializing it into
 * JSON, sending and receiving new JSON, and deserialize the new JSON back into a model data
 * representation.
 */
public class ProxyPlayer implements RefereePlayerInterface {

  private final Socket client;
  private String playerName;
  private final MazeJsonSerializer mazeSerializer;

  private final MethodJsonSerializer serializer;

  private final PrintWriter out;

  private final BufferedReader input;






  public ProxyPlayer(Socket client, String playerName) throws IOException {
    System.out.println("Proxy player: " + playerName);
    this.client = client;
    this.playerName = playerName;
    this.mazeSerializer = new MazeJsonSerializer();
    this.serializer = new MethodJsonSerializer();

    this.out = new PrintWriter(client.getOutputStream(), true);
    this.input = new BufferedReader(new InputStreamReader(client.getInputStream()));
  }

  @Override
  public Optional<TurnPlan> takeTurn(PlayerGameProjection game) throws IllegalPlayerActionException {

    while (true) {

      try {
        // JSON from Proxy Ref

        // if JSON is not valid, then throw error

        this.input.readLine();

        // turn JSON into TurnPlan

        // break
        break;


      } catch (Exception e) {
        this.out.println("takeTurn threw IOException");
      }



    }

    // return to send back to real Ref in Optional
    return Optional.empty();
  }

  @Override
  public boolean setup(PlayerGameProjection game, Position goal) {
    return false;
  }

  @Override
  public boolean win(boolean playerWon) {
    return false;
  }

  @Override
  public void returnHome(Position homeTile) {

  }

  @Override
  public void informGameEnd(GameStatus status, PlayerResult result) {

  }

  @Override
  public String getPlayerName() {
    return this.playerName;
  }

  @Override
  public boolean updateGoal(Position goal) {
    return false;
  }

  public Socket getSocket() {
    return this.client;
  }

  @Override
  public Board proposeBoard(int rows, int columns) {
    return new FlexibleBoard(columns, rows);
  }
}
