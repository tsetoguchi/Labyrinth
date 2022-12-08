package remote.client;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

import json.JsonDeserializer;
import json.JsonSerializer;
import model.Position;
import model.state.StateProjection;
import model.state.IState;
import player.IPlayer;
import referee.ITurn;
import remote.NetUtil;

/**
 * Represents the referee for a Client. Handles Json serialization and deserialization,
 * interpreting which methods need to be called, and calling them on the client.
 * As the client never calls methods on the Referee, there are no other public methods relating to the referee here.
 */
public class ProxyReferee implements Runnable {

  private final Socket socket;
  private final IPlayer player;

  public ProxyReferee(Socket socket, IPlayer p) throws IOException {
    this.socket = socket;
    this.player = p;
  }

  private void sendName(){
    NetUtil.sendOutput(this.player.getName(), this.socket);
  }

  @Override
  public void run() {
    this.sendName();

    StringBuilder str = new StringBuilder();

    while (!this.socket.isClosed()) {

      NetUtil.readInput(str, this.socket);

      try{
        this.execute(new JSONArray(str.toString()));
        str = new StringBuilder();
      } catch (JSONException ignore){
        //e.printStackTrace();
      }
    }
  }


  private void execute(JSONArray methodCall) throws JSONException {
    String methodName = methodCall.getString(0);
    JSONArray args = methodCall.getJSONArray(1);
    try {
      switch (methodName) {
        case "take-turn":
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

  private void handleTakeTurn(JSONArray args) throws JSONException, IOException {

    IState state = JsonDeserializer.state(args.getJSONObject(0));

    StateProjection projection = state.getStateProjection();

    ITurn turn = this.player.takeTurn(projection);



    String toSend;
    if(turn.isMove()){
      toSend = JsonSerializer.move(turn.getMove()).toString();
    } else{
      toSend = "\"PASS\"";
    }


    NetUtil.sendOutput(toSend, this.socket);

  }

  private void handleWin(JSONArray args) throws JSONException, IOException {
    boolean didWin = args.getBoolean(0);
    this.player.win(didWin);
    NetUtil.sendOutput("\"void\"", this.socket);
    this.socket.close();
  }

  private void handleSetup(JSONArray args) throws JSONException, IOException {
    Optional<StateProjection> maybeProjection;

    try{
      IState s = JsonDeserializer.state(args.getJSONObject(0));
      StateProjection projection = s.getStateProjection();
      maybeProjection = Optional.of(projection);
    } catch (JSONException ignore){
      maybeProjection = Optional.empty();
    }

    Position nextGoal = JsonDeserializer.coordinate(args.getJSONObject(1));

    this.player.setup(maybeProjection, nextGoal);
    NetUtil.sendOutput("\"void\"", this.socket);
  }










}
