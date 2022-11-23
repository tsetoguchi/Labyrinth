package remote.JSON;

import static java.lang.System.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class JsonDeserializeTest {

  public static void main(String[] args) throws IOException {

//    ObjectMapper om = new ObjectMapper();
//
//    remote.JSON.JsonBoard board = om.readValue(in, remote.JSON.JsonBoard.class);
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

    MethodJsonSerializer serializer = new MethodJsonSerializer();
    String winJson = serializer.generateWinJson(true);


    System.out.println(winJson);
  }

}
