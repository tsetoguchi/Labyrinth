package remote;

import java.io.BufferedReader;
import java.io.IOException;

public class NetUtil {

  public static void readNewInput(StringBuilder str, BufferedReader in){
    try {
      String toAdd = in.readLine();
      if(toAdd != null){
        str.append(toAdd);
      }
    } catch (IOException e){
      throw new RuntimeException(e);
    }
  }

}
