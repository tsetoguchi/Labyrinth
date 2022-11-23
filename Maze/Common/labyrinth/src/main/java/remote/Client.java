package remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Simple Client that can be given a name, and connects to a server sending it the name
 */
public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", NetUtil.defaultPort);

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        String name = args[0];

        while (true) {
            // not all json inputs will be line by line, this does not match the spec
            // figure out how to read json no matter how long it is
            String in = input.readLine();
            System.out.println(in);
            if (in.equals("close")) {
                System.out.println("Breaking");
                break;
            }
            out.println(name);

        }
        socket.shutdownOutput();
        socket.close();
        System.exit(1);
    }
}
