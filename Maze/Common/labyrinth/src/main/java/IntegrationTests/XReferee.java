package IntegrationTests;

import java.util.List;
import json.JsonDeserializer;
import json.JsonSerializer;
import model.Position;
import model.state.GameResults;
import model.state.IState;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import player.IPlayer;
import referee.Referee;

public class XReferee {

  public static void main(String[] args) throws JSONException {

    JSONTokener jsonTokener = IntegrationUtils.getInput();

    JSONArray playersJSON = (JSONArray) jsonTokener.nextValue();
    List<IPlayer> players = TestPlayer.jsonToTestPlayers(playersJSON);
//    Collections.reverse(players);

    JSONObject stateJSON = (JSONObject) jsonTokener.nextValue();
    IState game = JsonDeserializer.state(stateJSON);

    List<Position> goals = JsonDeserializer.goals(stateJSON);

    Referee referee = new Referee(game, players, goals);
    GameResults results = referee.runGame();
    System.out.print(JsonSerializer.gameResults(results));
    System.exit(0);

  }
}

