package remote.JSON;

import static java.lang.System.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class JsonDeserializeTest {

  public static void main(String[] args) throws IOException {

    ObjectMapper om = new ObjectMapper();

    JsonWin[] win = om.readValue(in, JsonWin[].class);


    System.out.println(win);
  }

}
