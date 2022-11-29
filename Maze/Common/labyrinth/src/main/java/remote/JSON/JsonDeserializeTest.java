package remote.JSON;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonDeserializeTest {

  public static void main(String[] args) throws IOException {

    ObjectMapper om = new ObjectMapper();
    String json = "[\"win\", [true]]";
    JsonWin jsonWin = om.readValue(json, JsonWin.class);

//    remote.JSON.JsonState state = om.readValue(in, remote.JSON.JsonState.class);
//
//    Object[] output = new Object[]{
//        board,
//        "hello",
//        state
//    };
//    System.out.println(om.writeValueAsString(output));
    //
//    JsonWin[] win = om.readValue(in, JsonWin[].class);

    JsonMethodSerializer serializer = new JsonMethodSerializer();
    String winJson = serializer.generateWinJson(true);


    System.out.println(om.writeValueAsString(jsonWin));
  }

}
