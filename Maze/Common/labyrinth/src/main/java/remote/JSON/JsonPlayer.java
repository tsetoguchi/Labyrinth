package remote.JSON;

import com.fasterxml.jackson.annotation.JsonProperty;
import model.Position;

import java.awt.*;

public class JsonPlayer {

    private final Position current;
    private final Position home;
    private final Color color;

    public JsonPlayer(
            @JsonProperty("current") Position current,
            @JsonProperty("home") Position home,
            @JsonProperty("color") Color color) {
        this.current = current;
        this.home = home;
        this.color = color;
    }

}
