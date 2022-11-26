package remote;

import com.fasterxml.jackson.core.JsonProcessingException;
import game.Exceptions.IllegalPlayerActionException;
import game.model.*;
import game.model.projections.PlayerGameProjection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import player.TurnPlan;
import protocol.serialization.JsonSerializer;
import referee.PlayerResult;
import player.IPlayer;
import remote.JSON.JsonMethodSerializer;

import java.net.Socket;
import java.util.Optional;

/**
 * The communication interface for a referee to communicate with a player through a network. The
 * functions will be responsible for taking in a model data representation, serializing it into
 * JSON, sending and receiving new JSON, and deserialize the new JSON back into a model data
 * representation.
 */
public class ProxyPlayerInterface implements IPlayer {

  private final Socket client;
  private String playerName;
  private final JsonSerializer mazeSerializer;

  private final JsonMethodSerializer serializer;

  private final PrintWriter out;

  private final BufferedReader input;


  public ProxyPlayerInterface(Socket client, String playerName) throws IOException {
    //System.out.println("Proxy player: " + playerName);
    this.client = client;
    this.playerName = playerName;
    this.mazeSerializer = new JsonSerializer();
    this.serializer = new JsonMethodSerializer();

    this.out = new PrintWriter(client.getOutputStream(), true);
    this.input = new BufferedReader(new InputStreamReader(client.getInputStream()));
  }

  @Override
  public Optional<TurnPlan> takeTurn(PlayerGameProjection game) throws IllegalPlayerActionException {

    // Converts call into JSON and sends it to the client
    try {
      String json = this.serializer.generateTakeTurnJson(game);
      this.out.print(json);
    } catch (JsonProcessingException e) {
      ////
    }



    String response;
    // Wait for a response
    while (true) {
      try {
        response = this.input.readLine();
        break;
      } catch (Exception e) {
        this.out.println("takeTurn threw IOException");
      }
    }

    // Deserialize response into a TurnPlan
    //Optional<TurnPlan> turnPlan = Serializer.getTurnPlan(response);

    // return to send back to real Ref in Optional
    return Optional.empty();
  }

  @Override
  public boolean setup(Optional<PlayerGameProjection> game, Position goal) {

    String json = null;
    try {
      json = this.serializer.generateSetupJson(game, goal);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    this.out.print(json);

    // Wait for a response to see if message was received
    while (true) {
      try {
        String response = this.input.readLine();
        break;
      } catch (Exception e) {
        this.out.println("takeTurn threw IOException");
      }
    }

    // Return ACK of true signalling success
    return true;
  }

  @Override
  public boolean win(boolean playerWon) {

    try {
      String json = this.serializer.generateWinJson(playerWon);
      this.out.print(json);
    } catch (JsonProcessingException e) {

    }


    // Wait for a response to see if message was received
    while (true) {
      try {
        String response = this.input.readLine();
        break;
      } catch (Exception e) {
        this.out.println("takeTurn threw IOException");
      }
    }

    // Return ACK of true signalling success
    return true;
  }

  /**
   * Legacy call for when a player has reached their goal and should return home.
   */
  @Override
  public void returnHome(Position homeTile) {

  }

  /**
   * Legacy call from old design to give game results to Player.
   */
  @Override
  public void informGameEnd(GameStatus status, PlayerResult result) {

  }

  @Override
  public String getPlayerName() {
    return this.playerName;
  }

  /**
   * Legacy method used for testing in other interface implementations.
   */
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

  private boolean parseTrueOrFalse(String in) {
    if (in.equals("true")) {
      return true;
    }
    else if (in.equals("false")) {
      return false;
    }
    else {
      throw new IllegalArgumentException("Bad return");
    }
  }
}
