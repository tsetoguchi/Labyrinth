package remote;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class NetUtil {

  public static void readInput(StringBuilder str, Socket sock) {
    DataInputStream in;
    try{
      InputStream input = sock.getInputStream();
      in = new DataInputStream(input);


      String message = in.readUTF();
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

      //sock.shutdownOutput();

    } catch(Exception e){
      e.printStackTrace();
    }
  }

}
