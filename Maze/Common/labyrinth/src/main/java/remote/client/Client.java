package remote.client;

import player.IPlayer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Optional;

/**
 *
 */
public class Client implements Runnable {
    private final IPlayer player;
    private final InetAddress address;
    private final int port;

    public Client(IPlayer player, InetAddress address, int port) {
        this.player = player;
        this.address = address;
        this.port = port;
    }

    /**
     * Starts the Client, creating and connecting to a Socket and sending this Client's name.
     * Also initializes a ProxyReferee to be used as a translation layer.
     *
     * Must be called before any other method. A refactor could be done to place the socket within ProxyReferee,
     * making all communication take place within it.
     */
    public void run() {
        Optional<Socket> socket = Optional.empty();
        while(socket.isEmpty()){
            try {
                Socket sock = new Socket(this.address, this.port);
                socket = Optional.of(sock);
            } catch (IOException ignore){}
        }

        try {
            //TODO debug Client
            //System.out.println(this.player.getName() + " connected");
            ProxyReferee proxyRef = new ProxyReferee(socket.get(), this.player);
            proxyRef.run();
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
