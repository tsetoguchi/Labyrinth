package remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

  public static void main(String[] args) throws IOException {

    Socket socket = new Socket("remote client", NetUtil.defaultPort);

    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    BufferedReader keyInput = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

    while (true) {
      out.println("Client connected!");
      String keyboard = keyInput.readLine();

      out.println(keyboard);

      if (keyboard.equals("quit")) {
        break;
      }
    }

    socket.close();

  }

}
