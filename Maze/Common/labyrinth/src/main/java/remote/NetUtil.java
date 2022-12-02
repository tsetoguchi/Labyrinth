package remote;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;

public class NetUtil {

  public static void readNewInput(StringBuilder str, DataInputStream in) {
    try {
      System.out.println("begin");
      if(in.){
        String toAdd = in.readUTF();
        str.append(toAdd);
        System.out.println("end");

      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
