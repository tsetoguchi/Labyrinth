package remote.JSON;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape=JsonFormat.Shape.ARRAY)
public class JsonTakeTurn {

  @JsonProperty("MName")
  private final String mName;

  @JsonProperty("Arguments")
  private final JsonState[] arguments;

  public JsonTakeTurn(@JsonProperty("Arguments") JsonState state) {
    this.mName = "take-turn";
    this.arguments = new Object[1];
    this.arguments[0] = state;
  }


  public JsonState getJsonState() {

  }
}
