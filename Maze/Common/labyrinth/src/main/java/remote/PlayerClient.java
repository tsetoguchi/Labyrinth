//package remote;
//
//import player.Player;
//import player.RiemannStrategy;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//
///**
// *
// */
//public class PlayerClient {
//    String name;
//    Player player;
//    Socket socket; // Should be null until a connection is wanted
//
//    PrintWriter out;
//
//    public PlayerClient(String name) {
//        this.name = name;
//        this.player = new Player(name, new RiemannStrategy());
//        this.socket = null;
//    }
//
//    /**
//     * Starts the PlayerClient, creating and connecting to a Socket and sending this PlayerClient's name.
//     * Also initializes a ProxyReferee to be used as a translation layer.
//     *
//     * Must be called before any other method. A refactor could be done to place the socket within ProxyReferee,
//     * making all communication take place within it.
//     */
//    public void run() {
//        try {
//            this.socket = new Socket("localhost", NetUtil.defaultPort);
//
//            this.out = new PrintWriter(socket.getOutputStream(), true);
//            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//            this.out.print(this.name);
//            while (true) {
//                String in = input.readLine();
//
//                ProxyReferee proxyReferee = new ProxyReferee(this.player);
//                proxyReferee.interpretJson(in);
//                System.out.println(in);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void sendMessage(String message) {
//        this.out.print(message);
//    }
//
//    public void close() {
//        try {
//            this.socket.shutdownOutput();
//            this.socket.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
