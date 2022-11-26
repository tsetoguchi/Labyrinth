package remote;

import game.model.GameResults;
import referee.IReferee;
import referee.Referee;
import player.IPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

public class Server {

    private static final List<IPlayer> proxyPlayers = new ArrayList<>();

    private static ExecutorService service;

    private static int port = NetUtil.defaultPort;


    public static void main(String[] args) throws IOException {

        service = Executors.newCachedThreadPool();

        try {
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Created server socket");
            beginSignUp(ss);
        } catch (Throwable throwable) {
//            messageClients("Game failed to start.");
        }

        if (proxyPlayers.size() >= 2) {

            IReferee referee = new Referee(proxyPlayers);
            // start game
            GameResults result = referee.runGame();
            System.out.println(result.resultsJson());;
        }
        else {
            // return empty arrays
            System.out.println("[ [], [] ]");
        }

    }

    private static void beginSignUp(ServerSocket ss) {

//    List<ProxyPlayer> proxyPlayers = new ArrayList<>();
        long endTime = System.currentTimeMillis() + (NetUtil.defaultWaitPeriodSeconds * 1000);
        while (true) {
            System.out.println("beginSignUp()");
            System.out.println((endTime - System.currentTimeMillis()) / 1000);

            long waitTimeRemaining = endTime - System.currentTimeMillis();

            // 20 seconds passed and less than 2 players signed up
            if (System.currentTimeMillis() > endTime && proxyPlayers.size()  < 2) {
                secondWaitPhase(ss);
                endTime = System.currentTimeMillis() + (NetUtil.defaultWaitPeriodSeconds * 1000);
                break;
            }

            // 20 seconds passed but at least 2 players signed up
            if (System.currentTimeMillis() > endTime && proxyPlayers.size() >= 2) {
                System.out.println("20 seconds passed and at least 2 players signed up.");
                break;
            }
            // 6 players signed up
            if (proxyPlayers.size()  >= 6) {
                System.out.println("6 players signed up.");
                break;
            }

            addPlayerIfPresent(signUpClient(ss, waitTimeRemaining));
        }
    }


    private static Optional<ProxyPlayerInterface> signUpClient(ServerSocket ss, long waitTimeRemaining) {

        System.out.println("signUpClient()");
        System.out.println(waitTimeRemaining / 1000);
        long timer = System.currentTimeMillis();
        Optional<ProxyPlayerInterface> player = null;

        // If the client failed to connect
        try {
            player = awaitSignUp(ss, waitTimeRemaining);
        } catch (IOException e){
            return Optional.empty();
        }

        return player;
    }


    private static void secondWaitPhase(ServerSocket ss) {
        System.out.println("secondWaitPhase()");
        long endTime = System.currentTimeMillis() + (NetUtil.defaultWaitPeriodSeconds * 1000);

        while (true) {

            // TODO:  If at most one player signs up by the end of the second waiting period,
            //  the server doesn’t run a game and instead delivers a simple default result:  [ [], [] ].

            if (System.currentTimeMillis() > endTime) {
                break;
            }

            if (proxyPlayers.size()  >= 6) {
                break;
            }

            addPlayerIfPresent(signUpClient(ss, endTime - System.currentTimeMillis()));
        }
    }

    private static void addPlayerIfPresent(Optional<ProxyPlayerInterface> proxyPlayer) {
        if (proxyPlayer.isPresent()) {
            proxyPlayers.add(proxyPlayer.get());
        }
    }

    private static Callable<String> awaitName(ProxyPlayerInterface player) {
        Callable<String> callableString = () -> {
            return player.getPlayerName();
        };
        return callableString;
    }

    private static Optional<ProxyPlayerInterface> awaitSignUp(ServerSocket ss, long waitTimeRemaining) throws IOException {

        System.out.println("awaitSignUp()");
        Callable<Socket> acceptTimeOut = () -> {
            Socket client = ss.accept();
            return client;
        };

        Socket client = null;
        try {
            client = service.submit(acceptTimeOut).get(waitTimeRemaining, TimeUnit.MILLISECONDS);
        } catch (Throwable e) {
            System.out.println("Accept timed out");
            return Optional.empty();
        }


        System.out.println("Client connected");
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        messageClient("Please choose a name: ", out);

        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));


        Socket finalClient = client;

        Callable<ProxyPlayerInterface> callableProxyPlayer = () -> {
            String clientName = input.readLine();
            ProxyPlayerInterface proxyPlayer = new ProxyPlayerInterface(finalClient, clientName);
            return proxyPlayer;
        };

        try {
            return Optional.of(service.submit(callableProxyPlayer).get(NetUtil.defaultPlayerSignUpSeconds, TimeUnit.SECONDS));
        } catch (Throwable throwable) {
            messageClient("close", out);
            client.close();
            return Optional.empty();
        }
    }

//    private static void messageClients(String message) {
//        try {
//            for (ProxyPlayer player : proxyPlayers) {
//                player.getSocket().getOutputStream().write(message.getBytes());
//            }
//        } catch (Throwable throwable) {
//            System.out.println("Server failed while messaging clients.");
//        }
//    }

    private static void messageClient(String message, PrintWriter out) {
        try {
            out.println(message);
        } catch (Throwable throwable) {
            System.out.println("Failed to message client.");
        }
    }

    private static void fetchPort(String[] args) {
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
    }


}
