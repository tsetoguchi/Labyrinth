package remote.JSON;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonWin {

  private final String mName;

  @JsonProperty("win")
  private final boolean[] win;


  public JsonWin(String mName, boolean win) {
    this.mName = mName;
    this.win = new boolean[1];
    this.win[0] = win;
  }

  public JsonWin(boolean win) {
    this("win", win);
  }
}
