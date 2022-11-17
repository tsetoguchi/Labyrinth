package remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MockClient {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", NetUtil.defaultPort);

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        String name = args[0];

        while (true) {
            String in = input.readLine();
            System.out.println(in);
            if (in.equals("close")) {
                System.out.println("Breaking");
                break;
            }
            out.println(name);
//            out.println(stdIn.readLine());

            if (stdIn.equals("quit")) {
                break;
            }
        }
        socket.shutdownOutput();
        socket.close();
        System.exit(1);
    }
}
