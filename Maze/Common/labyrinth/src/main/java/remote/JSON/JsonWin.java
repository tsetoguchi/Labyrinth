package remote.JSON;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape=JsonFormat.Shape.ARRAY)
public class JsonWin {

  @JsonProperty("MName")
  private final String mName;

  @JsonProperty("Arguments")
  private final boolean[] win;


  public JsonWin(@JsonProperty("MName")String mName,@JsonProperty("Arguments") boolean[] win) {
    this.mName = mName;
    this.win = win;
  }

  public JsonWin(boolean[] win) {
    this("win", win);
  }
}
