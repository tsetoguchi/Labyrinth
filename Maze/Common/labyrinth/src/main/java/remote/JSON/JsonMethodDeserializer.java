package remote.JSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class JsonMethodDeserializer {

  private final ObjectMapper mapper;


  public JsonMethodDeserializer() {
    this.mapper = new ObjectMapper();
  }

  public JsonWin generateJsonWin(String json) throws IOException {
    return this.mapper.readValue(json, JsonWin.class);
  }

  public JsonTakeTurn generateTakeTurnJson(String json) throws IOException {
    return this.mapper.readValue(json, JsonTakeTurn.class);
  }

  public JsonSetup generateSetup(String json) throws IOException {
    return this.mapper.readValue(json, JsonSetup.class);
  }


}
