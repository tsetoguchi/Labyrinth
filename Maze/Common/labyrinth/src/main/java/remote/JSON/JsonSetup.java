package remote.JSON;

import com.fasterxml.jackson.annotation.JsonFormat;
import model.Position;

@JsonFormat(shape=JsonFormat.Shape.ARRAY)
public class JsonSetup {

  private final String mName;

  private final Object[] arguments;


  public JsonSetup(JsonState state, Position coordinate) {
    this.mName = "setup";
    this.arguments = new Object[2];
    this.arguments[0] = state;
    this.arguments[1] = coordinate;
  }
}
