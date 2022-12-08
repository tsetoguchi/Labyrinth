package remote;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NetUtil {

  public static void readInput(StringBuilder str, Socket sock) {
    DataInputStream in;
    try{
      InputStream input = sock.getInputStream();
      in = new DataInputStream(input);

      String message = in.readUTF();
      //TODO debug message:
      System.out.println(message);
      str.append(message);
    } catch(Exception ignore){
    }
  }


  public static void sendOutput(String str, Socket sock) {
    DataOutputStream out;
    try{
      OutputStream output = sock.getOutputStream();
      out = new DataOutputStream(output);

      out.writeUTF(str);
      out.flush();

    } catch(Exception e){
      e.printStackTrace();
    }
  }

}
