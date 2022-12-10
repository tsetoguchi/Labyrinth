package remote.server;

import java.net.Socket;
import java.util.Optional;
import json.JsonDeserializer;
import json.JsonSerializer;
import model.Position;
import model.board.Board;
import model.board.IBoard;
import model.state.StateProjection;
import org.json.JSONArray;
import org.json.JSONException;
import player.IPlayer;
import referee.ITurn;
import referee.Pass;
import remote.NetUtil;

/**
 * The communication interface for a referee to communicate with a player through a network. The
 * functions will be responsible for taking in a model data representation, serializing it into
 * JSON, sending and receiving new JSON, and deserialize the new JSON back into a model data
 * representation.
 */
public class ProxyPlayer implements IPlayer {

  // The socket connected to the corresponding ProxyReferee
  protected final Socket socket;
  private final String playerName;

  public ProxyPlayer(Socket socket, String playerName) {
    this.socket = socket;
    this.playerName = playerName;
  }


  /**
   * Prompts the ProxyReferee to ask the corresponding Player for a turn. Then waits for a response
   * from the ProxyReferee to send the player's turn back to the IReferee. Once a response is
   * received, it is deserialized into an ITurn and returned.
   */
  @Override
  public ITurn takeTurn(StateProjection game) {

    try {
      JSONArray toSend = JsonSerializer.takeTurn(game);
      NetUtil.sendOutput(toSend.toString(), this.socket);
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }

    StringBuilder response = new StringBuilder();
    while (true) {
      NetUtil.readInput(response, this.socket);

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

  /**
   * Prompts the ProxyReferee to tell the corresponding Player to setup. Once a "void" response is
   * received, true is returned.
   */
  @Override
  public boolean setup(Optional<StateProjection> game, Position goal) {

    try {
      JSONArray toSend = JsonSerializer.setup(game, goal);
      NetUtil.sendOutput(toSend.toString(), this.socket);

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

  /**
   * Prompts the ProxyReferee to tell the corresponding Player whether they won or not. Once a
   * "void" response is received, true is returned.
   */
  @Override
  public boolean win(boolean won) {

    JSONArray toSend = JsonSerializer.win(won);
    NetUtil.sendOutput(toSend.toString(), this.socket);

    StringBuilder response = new StringBuilder();
    while (true) {
      NetUtil.readInput(response, this.socket);

      if (response.toString().equals("\"void\"")) {
        return true;
      }

    }
  }

  /**
   * Retrieves the name of the player.
   */
  @Override
  public String getName() {
    return this.playerName;
  }

  /**
   * Retrieves a random board with the given amount of rows (height) and columns (width).
   */
  @Override
  public IBoard proposeBoard(int rows, int columns) {
    return new Board(columns, rows);
  }


}
