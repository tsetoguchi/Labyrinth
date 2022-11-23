package remote.JSON;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTile {

    private final String connectors;

    private final String gem1;

    private final String gem2;

    public JsonTile(@JsonProperty("tilekey") String connectors,
                    @JsonProperty("1-image") String gem1,
                    @JsonProperty("2-image") String gem2) {
        this.connectors = connectors;
        this.gem1 = gem1;
        this.gem2 = gem2;
    }
}
