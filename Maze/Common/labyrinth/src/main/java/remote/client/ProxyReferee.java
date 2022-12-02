package remote.client;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Scanner;

import json.JsonDeserializer;
import json.JsonSerializer;
import model.Position;
import model.projections.StateProjection;
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
  private final DataOutputStream out;
  private final DataInputStream in;
  private final IPlayer player;

  public ProxyReferee(Socket socket, IPlayer p) throws IOException {
    this.socket = socket;
    this.out = new DataOutputStream(this.socket.getOutputStream());
    this.in = new DataInputStream(this.socket.getInputStream());
    this.player = p;
  }

  private void sendName() throws IOException{
    this.out.writeUTF(this.player.getName());
    this.out.flush();
  }

  private static byte[] toBytes(String str){
    return str.getBytes(StandardCharsets.UTF_8);
  }


  @Override
  public void run() {
    try {
      this.sendName();
    } catch (IOException e) {
      e.printStackTrace();
    }

    StringBuilder str = new StringBuilder();

    while (!this.socket.isClosed()) {

      NetUtil.readInput(str, this.socket);
      System.out.println(str);

      try{
        this.execute(new JSONArray(str.toString()));
        str = new StringBuilder();
      } catch (JSONException e){
        //e.printStackTrace();
      }
    }
  }


  private void execute(JSONArray methodCall) throws JSONException {
    System.out.println("1");
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


  private void handleSetup(JSONArray args) throws JSONException, IOException {
    Optional<StateProjection> maybeProjection;

    try{
      IState s = JsonDeserializer.state(args.getJSONObject(0));
      StateProjection projection = s.getStateProjection();
      maybeProjection = Optional.of(projection);
    } catch (JSONException ignore){
      maybeProjection = Optional.empty();
    }

    Position nextGoal = JsonDeserializer.position(args.getJSONObject(1));

    this.player.setup(maybeProjection, nextGoal);
    NetUtil.sendOutput("\"void\"", this.socket);
  }


  private void handleWin(JSONArray args) throws JSONException, IOException {
    boolean didWin = args.getBoolean(0);
    this.player.win(didWin);
    NetUtil.sendOutput("\"void\"", this.socket);
    this.socket.close();
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




}
