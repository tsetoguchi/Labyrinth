package protocol.serialization;

import game.model.PrivateState;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import player.IPlayer;

public class JsonDeserializer {

  public static PrivateState jsonToState(JSONObject game) {
    return;
  }

  public List<IPlayer> jsonToPlayerSpec(JSONArray players) {

    for (int i = 0; i < players.length(); i++) {



    }

//    for(int i=0; i<players.length(); i++){
//
//      JSONObject pJSON = plmtJSON.getJSONObject(i);
//      String colorString = pJSON.getString("color");
//      Color color = Utils.stringToColor(colorString);
//      JSONObject currentJSON = pJSON.getJSONObject("current");
//      Coordinate current = Utils.getCoordinateJSON(currentJSON);
//
//      JSONObject homeJSON = pJSON.getJSONObject("home");
//      Coordinate home = Utils.getCoordinateJSON(homeJSON);
//
//      JSONObject goToJSON = pJSON.getJSONObject("goto");
//      Coordinate goTo = Utils.getCoordinateJSON(goToJSON);
//
//      JSONArray player = players.getJSONArray(i);
//      String name = player.getString(0);
//      String strategy = player.getString(1);
//      String bad = "none";
//      int count = 1;
//      boolean loop = false;
//      if (player.length() > 2) {
//        bad = player.getString(2);
//      }
//      if (player.length() > 3) {
//        count = player.getInt(3);
//        loop = true;
//      }
//
//      BrokenPlayer p;
//      if(strategy.equals("Euclid")){
//        p = new BrokenPlayer(new EuclidStrategy(), name, color, bad, count, loop);
//      } else if(strategy.equals("Riemann")){
//        p = new BrokenPlayer(new RiemannStrategy(), name, color, bad, count, loop);
//      } else{
//        throw new JSONException("Invalid strategy" + strategy);
//      }
//      ref.addPlayerToGame(p, color, goTo, home, current);
//    }
  }

}
