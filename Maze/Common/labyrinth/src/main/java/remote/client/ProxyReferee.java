package remote.client;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import json.JsonDeserializer;
import json.JsonSerializer;
import model.Position;
import model.projections.StateProjection;
import model.state.IState;
import player.IPlayer;
import referee.ITurn;

/**
 * Represents the referee for a Client. Handles Json serialization and deserialization,
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

  private static byte[] toBytes(String str){
    return str.getBytes(StandardCharsets.UTF_8);
  }


  @Override
  public void run() {
    StringBuilder str = new StringBuilder();

    while (!this.socket.isClosed()) {
      this.readNewInput(str);

      try{
        this.execute(new JSONArray(str.toString()));
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


  private void execute(JSONArray methodCall) throws JSONException {
    String methodName = methodCall.getString(0);
    JSONArray args = methodCall.getJSONArray(1);
    try {
      switch (methodName) {
        case "take_turn":
          this.handleTakeTurn(args);
          return;
        case "win":
          this.handleWin(args);
          return;
        case "setup":
          this.handleSetup(args);
      }
    }catch (IOException e){
      throw new RuntimeException(e);
    }
  }


  private void handleSetup(JSONArray args) throws JSONException, IOException {
    Optional<StateProjection> maybeProjection;

    try{
      IState s = JsonDeserializer.jsonToState(args.getJSONObject(0));
      StateProjection projection = s.getStateProjection();
      maybeProjection = Optional.of(projection);
    } catch (JSONException ignore){
      maybeProjection = Optional.empty();
    }

    Position nextGoal = JsonDeserializer.jsonToPosition(args.getJSONObject(1));

    this.player.setup(maybeProjection, nextGoal);
    this.out.write(toBytes("\"void\""));
  }


  private void handleWin(JSONArray args) throws JSONException, IOException {
    boolean didWin = args.getBoolean(0);
    this.player.win(didWin);
    this.out.write(toBytes("\"void\""));
    this.socket.close();
  }


  private void handleTakeTurn(JSONArray args) throws JSONException, IOException {
    IState state = JsonDeserializer.jsonToState(args.getJSONObject(0));
    StateProjection projection = state.getStateProjection();
    ITurn turn = this.player.takeTurn(projection);
    if(turn.isMove()){
      this.out.write(toBytes(JsonSerializer.moveToJson(turn.getMove()).toString()));
    } else{
      this.out.write(toBytes("\"PASS\""));
    }
  }




}
