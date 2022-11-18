package remote.json;


import com.fasterxml.jackson.annotation.JsonProperty;


public class JsonBoard {

    private final String[][] connectors;

    private final String[][][] treasures;

    public JsonBoard(@JsonProperty("connectors") String[][] connectors,
                     @JsonProperty("treasures") String[][][] treasures) {
        this.connectors = connectors;
        this.treasures = treasures;
    }


}
