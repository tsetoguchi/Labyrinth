package remote.JSON;

public class JsonTakeTurn {

  private final String mName;

  private final Object[] arguments;

  public JsonTakeTurn(JsonState state) {
    this.mName = "take-turn";
    this.arguments = new Object[1];
    this.arguments[0] = state;
  }
}
