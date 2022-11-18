package remote;

import player.Player;

/**
 * Represents the referee for a PlayerClient. Handles Json serialization and deserialization,
 * interpreting which methods need to be called, and calling them on the client.
 * As the client never calls methods on the Referee, there are no other public methods relating to the referee here.
 */
public class ProxyReferee {

    Player player;
    //MethodJsonDeserializer methodJsonDeserializer;

    public ProxyReferee(Player player) {
        this.player = player;
    }

    /**
     * Deserialize Json and call the relevant Player methods.
     */
    public void interpretJson(String in) {
        boolean setup = false;
        boolean takeTurn = false;
        boolean win = false;

        // JSon deserializing is not complete yet, but if it were we would call player.method with the deserialized
        // arguments.
    }
}
