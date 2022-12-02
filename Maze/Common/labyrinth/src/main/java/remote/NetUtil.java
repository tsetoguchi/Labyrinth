package remote;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Scanner;

public class NetUtil {

  public static void readNewInput(StringBuilder str, DataInputStream in) {
    Scanner scanner = new Scanner(in);
    try {
      System.out.println("begin");
      if (scanner.hasNext()) {
        String toAdd = in.readUTF();
        str.append(toAdd);
        System.out.println("end");
      }
      scanner.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
