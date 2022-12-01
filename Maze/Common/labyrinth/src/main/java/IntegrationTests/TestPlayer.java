package IntegrationTests;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Position;
import model.projections.StateProjection;
import player.EuclideanStrategy;
import player.IPlayer;
import player.IStrategy;
import player.Player;
import player.RiemannStrategy;
import referee.ITurn;

public class TestPlayer extends Player {

    private final String bad;
    private final int count;
    private int current;


    public TestPlayer(String name, IStrategy strategy, String bad, int count){
        super(name, strategy);
        this.bad = bad;
        this.count = count;
        this.current = 0;
    }

    @Override
    public ITurn takeTurn(StateProjection game) {

        if(this.bad.equals("takeTurn")){
            this.current++;
            this.loop();
            this.errorOut();
        }

        return super.takeTurn(game);
    }

    @Override
    public boolean setup(Optional<StateProjection> game, Position goal) {

        if(this.bad.equals("setUp")){
            this.current++;
            System.out.println(this.current);
            this.loop();
            this.errorOut();
            System.out.println(this.current);
        }

        return super.setup(game, goal);
    }

    @Override
    public boolean win(boolean playerWon) {

        if(this.bad.equals("win")){
            this.current++;
            this.loop();
            this.errorOut();
        }

        return super.win(playerWon);
    }


    private void errorOut(){
        if(this.count == -1){
            int x = 1/0;
        }
    }

    private void loop(){
        if(this.count == this.current){
            while(true){
            }
        }
    }

    public static List<IPlayer> jsonToTestPlayers(JSONArray playersSpecJson) throws JSONException {
        List<IPlayer> players = new ArrayList<>();

        for(int i=0; i<playersSpecJson.length(); i++){
            JSONArray playerJson = playersSpecJson.getJSONArray(i);
            int length = playerJson.length();

            String name = playerJson.getString(0);
            String stratString = playerJson.getString(1);
            IStrategy strategy = null;
            if(stratString.equals("Euclid")){
                strategy = new EuclideanStrategy();
            } else if (stratString.equals("Riemann")){
                strategy = new RiemannStrategy();
            }

            String bad = "none";
            int count = -1;

            switch(length){
                case 4:
                    count = playerJson.getInt(3);
                case 3:
                    bad = playerJson.getString(2);
                    break;
            }

            players.add(new TestPlayer(name, strategy, bad, count));
        }


        return players;
    }


}