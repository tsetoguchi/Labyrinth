package remote;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import game.controller.IObserver;
import game.model.GameResults;
import game.model.PrivateGameState;
import java.io.IOException;
import java.util.List;
import player.Player;
import protocol.serialization.MazeJsonParser;
import referee.IReferee;
import referee.clients.RefereePlayerInterface;
import remote.json.JsonState;

/**
 * Represents the referee for a PlayerClient. Handles Json serialization and deserialization,
 * interpreting which methods need to be called, and calling them on the client.
 * As the client never calls methods on the Referee, there are no other public methods relating to the referee here.
 */
public class ProxyReferee implements IReferee {

    Player player;
    
    private final ObjectMapper mapper;

    public ProxyReferee(Player player) {
        this.player = player;
        this.mapper = new ObjectMapper();

    }

    /**
     * Deserialize Json and call the relevant Player methods.
     */
    public void interpretJson(String in) throws IOException {
        boolean setup = false;
        boolean takeTurn = false;
        boolean win = false;

        JsonParser parser = new JsonFactory().createParser(in);

        if (parser.nextToken().equals("setup")) {
            parser.
            JsonState state = new JsonState()
            player.setup()
        }



        // JSon deserializing is not complete yet, but if it were we would call player.method with the deserialized
        // arguments.
    }

    @Override
    public GameResults runGame() {
        return null;
    }

    @Override
    public void resume(PrivateGameState game, List<RefereePlayerInterface> players) {

    }

    @Override
    public void addObserver(IObserver observer) {

    }
}
