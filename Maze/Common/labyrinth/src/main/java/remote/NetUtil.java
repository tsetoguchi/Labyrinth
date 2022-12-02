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

      str.append(in.readUTF());

    } catch(Exception e){
      e.printStackTrace();
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
