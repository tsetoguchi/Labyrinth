package remote.JSON;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonState {

    private final JsonBoard board;
    private final JsonTile spare;

    private final JsonPlayer[] plmt;
    private final JsonSlideAndInsertRecord last;

    public JsonState(
            @JsonProperty("board") JsonBoard board,
            @JsonProperty("spare") JsonTile spare,
            @JsonProperty("plmt") JsonPlayer[] plmt,
            @JsonProperty("last") JsonSlideAndInsertRecord last) {
        this.board = board;
        this.spare = spare;
        this.plmt = plmt;
        this.last = last;
    }
}
