package remote.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import org.json.JSONArray;
import org.json.JSONException;

import json.JsonDeserializer;
import json.JsonSerializer;
import model.Exceptions.IllegalPlayerActionException;
import model.Position;
import model.board.Board;
import model.board.IBoard;

import java.io.IOException;

import model.projections.StateProjection;
import referee.ITurn;
import referee.Pass;
import player.IPlayer;
import remote.NetUtil;

import java.net.Socket;
import java.util.Optional;

/**
 * The communication interface for a referee to communicate with a player through a network. The
 * functions will be responsible for taking in a model data representation, serializing it into
 * JSON, sending and receiving new JSON, and deserialize the new JSON back into a model data
 * representation.
 */
public class ProxyPlayer implements IPlayer {

  private final String playerName;
  private final Socket client;
  private final DataInputStream in;


  public ProxyPlayer(Socket client, String playerName) throws IOException {
    this.client = client;
    this.playerName = playerName;
    this.in = new DataInputStream(client.getInputStream());
  }


  @Override
  public ITurn takeTurn(StateProjection game) throws IllegalPlayerActionException {

    try {
      JSONArray toSend = JsonSerializer.takeTurn(game);
      NetUtil.sendOutput(toSend.toString(), this.client);
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }


    StringBuilder response = new StringBuilder();
    while (true) {
      NetUtil.readInput(response, this.client);


      try {
        JSONArray moveJSON = new JSONArray(response.toString());
        return JsonDeserializer.move(moveJSON);
      } catch (JSONException ignore) {
      }

      if (response.toString().equals("\"PASS\"")) {
        return new Pass();
      }

    }
  }


  @Override
  public boolean setup(Optional<StateProjection> game, Position goal) {

    try {
      JSONArray toSend = JsonSerializer.setup(game, goal);
      NetUtil.sendOutput(toSend.toString(), this.client);

    } catch (JSONException e) {
      e.printStackTrace();
    }

    StringBuilder response = new StringBuilder();
    while (true) {
      NetUtil.readInput(response, this.client);


      if (response.toString().equals("\"void\"")) {
        return true;
      }

    }
  }

  @Override
  public boolean win(boolean won) {

    JSONArray toSend = JsonSerializer.win(won);
    NetUtil.sendOutput(toSend.toString(), this.client);

    StringBuilder response = new StringBuilder();
    while (true) {
      NetUtil.readInput(response, this.client);

      if (response.toString().equals("\"void\"")) {
        return true;
      }

    }
  }

  @Override
  public String getName() {
    return this.playerName;
  }

  @Override
  public IBoard proposeBoard(int rows, int columns) {
    return new Board(columns, rows);
  }


}
