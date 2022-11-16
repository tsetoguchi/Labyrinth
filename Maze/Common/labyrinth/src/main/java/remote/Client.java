package remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

  public static void main(String[] args) throws IOException {

    Socket socket = new Socket("localhost", NetUtil.defaultPort);

    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));



    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
//    OutputStream out = socket.getOutputStream();

//    Scanner input = new Scanner(System.in);

    Scanner response = new Scanner(socket.getInputStream());
//    String keyboard = keyInput.readLine();

    while (true) {
//      System.out.println(stdIn.readLine());

      System.out.println(input.readLine());

      out.println(stdIn);
      if (stdIn.equals("quit")) {
        break;
      }

    }

    socket.shutdownOutput();


    socket.close();

  }

}
