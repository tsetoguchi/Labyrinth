package json;

import model.state.IState;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import player.IPlayer;

public class JsonDeserializer {

  private static final Map<String, Color> colors;

  static {
    colors = new HashMap<>();
    colors.put("purple", new Color(218, 112, 214));
    colors.put("orange", Color.ORANGE);
    colors.put("pink", Color.PINK);
    colors.put("red", Color.RED);
    colors.put("blue", Color.BLUE);
    colors.put("green", Color.GREEN);
    colors.put("yellow", Color.YELLOW);
    colors.put("white", Color.WHITE);
    colors.put("black", Color.BLACK);
  }

  public static IState jsonToState(JSONObject game) {

    return null;
  }

  public static List<IPlayer> jsonToPlayerSpec(JSONArray players) throws JSONException {
    List<IPlayer> result = new ArrayList<>();

    for(int i=0; i<players.length(); i++){
      JSONArray player = players.getJSONArray(i);
      String name = player.getString(0);
      String strategy = player.getString(1);
      String bad = "none";
      int count = 1;
      boolean loop = false;
      if (player.length() > 2) {
        bad = player.getString(2);
      } else{
        IPlayer p = new TestPlayer(name, Strategy);
      }

      if (player.length() == 4) {
        count = player.getInt(3);
        loop = true;
      }



    return result;
  }

}
