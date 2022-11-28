package remote;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.model.GameResults;
import game.model.PrivateGameState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import player.IPlayer;
import referee.IReferee;
import referee.clients.RefereePlayerInterface;
import remote.json.JsonState;

/**
 * Represents the referee for a PlayerClient. Handles Json serialization and deserialization,
 * interpreting which methods need to be called, and calling them on the client.
 * As the client never calls methods on the Referee, there are no other public methods relating to the referee here.
 */
public class ProxyReferee implements Runnable {

  private final Socket socket;
  private final OutputStream out;
  private final BufferedReader in;
  private final IPlayer player;

  public ProxyReferee(Socket socket, IPlayer p) throws IOException {
    this.socket = socket;
    this.out = socket.getOutputStream();
    this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    this.player = p;
    this.sendName();
  }

  private void sendName() throws IOException{
    this.out.write(toBytes("\"" + this.player.getName() + "\""));
  }

  public static byte[] toBytes(String str){
    return str.getBytes(StandardCharsets.UTF_8);
  }



  @Override
  public void run() {
    StringBuilder str = new StringBuilder();
    while (!this.socket.isClosed()) {

      this.readNewInput(str);

      try{
        JSONArray command = this.buildCommandFromInput(str.toString());
        this.execute(command);
        str = new StringBuilder();
      } catch (JSONException ignore){}
    }
  }

  private void readNewInput(StringBuilder str){
    try {
      String toAdd = this.in.readLine();
      if(toAdd != null){
        str.append(toAdd);
      }
    } catch (IOException e){
      throw new RuntimeException(e);
    }
  }


  private JSONArray buildCommandFromInput(String input) throws JSONException{
    JSONArray command;
    command = new JSONArray(input);
    ApiOperation.fromString(command.getString(0));
    command.getJSONArray(1);
    return command;
  }


  private void execute(JSONArray command) throws JSONException {
    ApiOperation method = ApiOperation.fromString(command.getString(0));
    JSONArray args = command.getJSONArray(1);
    try {
      switch (method) {
        case take_turn:
          this.executeTakeTurn(args);
          return;
        case win:
          this.executeWin(args);
          return;
        case setup:
          this.executeSetup(args);
      }
    }catch (IOException e){
      throw new RuntimeException(e);
    }
  }


  private void executeSetup(JSONArray args) throws JSONException, IOException {
    Optional<State> maybeState = Optional.empty();
    try{
      State s = JSONConverter.stateFromJSON(args.getJSONObject(0));
      maybeState = Optional.of(s);
    } catch (JSONException ignore){}

    Coordinate destination = JSONConverter.coordinateFromJSON(args.getJSONObject(1));

    this.player.setup(maybeState, destination);
    this.out.write(Utils.toBytes("\"void\""));
  }


  private void executeWin(JSONArray args) throws JSONException, IOException {
    boolean didWin = args.getBoolean(0);
    this.player.won(didWin);
    this.gameOver = true;
    this.out.write(Utils.toBytes("\"void\""));
  }


  private void executeTakeTurn(JSONArray args) throws JSONException, IOException {
    State state = JSONConverter.stateFromJSON(args.getJSONObject(0));
    Action a = this.player.takeTurn(state);
    if(a.isMove()){
      this.out.write(Utils.toBytes(JSONConverter.moveActionToJSON(a.getMove()).toString()));
    } else{
      this.out.write(Utils.toBytes("\"PASS\""));
    }
  }




}
