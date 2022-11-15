package remote;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final List<ProxyPlayer> players;
    private static int counter;
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);

        counter = 0;



        try {
            ServerSocket ss = new ServerSocket(port);
            signUpClients();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void signUpClients() {
        while (true) {
            counter++;

            exceptionHandler()

            ProxyPlayer pp = .....
            players.add()
        }
    }


    private static void exceptionHandler() {

    }


}
