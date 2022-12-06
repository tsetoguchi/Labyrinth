package IntegrationTests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

import json.JsonDeserializer;
import json.JsonSerializer;
import model.Position;
import model.state.GameResults;
import model.state.IState;
import player.IPlayer;
import referee.Referee;

public class XBad2 {

  public static void main(String[] args) throws JSONException {

    JSONTokener jsonTokener = IntegrationUtils.getInput();

    JSONArray playersJSON = (JSONArray) jsonTokener.nextValue();
    List<IPlayer> players = TestPlayer.jsonToTestPlayers(playersJSON);

    JSONObject stateJSON = (JSONObject) jsonTokener.nextValue();
    IState game = JsonDeserializer.state(stateJSON);

    JSONArray plmt = stateJSON.getJSONArray("plmt");
    List<Position> goals = JsonDeserializer.goals(plmt, stateJSON);

    Referee referee = new Referee(game, players, goals);
    GameResults results = referee.runGame();
    System.out.print(JsonSerializer.gameResults(results));
    System.exit(0);

  }
}

