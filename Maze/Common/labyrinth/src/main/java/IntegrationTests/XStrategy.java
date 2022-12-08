package IntegrationTests;

import json.JsonDeserializer;
import model.Position;
import model.state.IState;
import model.state.StateProjection;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import player.EuclideanStrategy;
import player.IStrategy;
import player.RiemannStrategy;


public class XStrategy {
    public static void main(String[] args) throws JSONException {

      JSONTokener jsonTokener = IntegrationUtils.getInput();


      String strategy = (String) jsonTokener.nextValue();
      IStrategy s;
      if(strategy.equals("Riemann")){
        s = new RiemannStrategy();
      } else{
        s = new EuclideanStrategy();
      }

      JSONObject stateJSON = (JSONObject) jsonTokener.nextValue();
      IState state = JsonDeserializer.state(stateJSON);
      StateProjection sp = state.getStateProjection();

      JSONObject goalJSON = (JSONObject) jsonTokener.nextValue();
      Position goal = JsonDeserializer.coordinate(goalJSON);

      System.out.println(s.createTurn(sp, goal));


    }
}
