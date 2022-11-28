package remote.client;

import player.IPlayer;
import player.Player;
import player.RiemannStrategy;
import remote.ProxyReferee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Optional;

/**
 *
 */
public class PlayerClient implements Runnable {
    private final IPlayer player;
    private final InetAddress address;
    private final int port;
    PrintWriter out;

    public PlayerClient(IPlayer player, InetAddress address, int port) {
        this.player = player;
        this.address = address;
        this.port = port;
    }

    /**
     * Starts the PlayerClient, creating and connecting to a Socket and sending this PlayerClient's name.
     * Also initializes a ProxyReferee to be used as a translation layer.
     *
     * Must be called before any other method. A refactor could be done to place the socket within ProxyReferee,
     * making all communication take place within it.
     */
    public void run() {
        InetSocketAddress socketAddress = new InetSocketAddress(this.address, this.port);
        Optional<Socket> socket = Optional.empty();
        while(socket.isEmpty()){
            try {
                Socket trySock = new Socket();
                trySock.connect(socketAddress);
                socket = Optional.of(trySock);
            } catch (IOException ignore){}
        }

        try {
            ProxyReferee proxyRef = new ProxyReferee(socket.get(), this.player);
            proxyRef.run();
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
