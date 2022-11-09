package xtcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class XTcp {
    public static void main(String[] args) throws IOException {

        // https://www.codejava.net/java-se/networking/java-socket-server-examples-tcp-ip
        ServerSocket server = new ServerSocket(Integer.parseInt(args[0]));
        log(args[0]);
        Socket sock = server.accept();

        String json = readFromSocket(sock);

        String output = XJson.getJsonFromInput(json);
        writeToSocket(output, sock);
        sock.close();
        server.close();
    }

    private static String readFromSocket(Socket sock) throws IOException {
        InputStream inStream = sock.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        StringBuilder jsonBuilder = new StringBuilder();

        boolean keepReading = true;
        while (keepReading) {
            String line = reader.readLine();
            if (line != null) {
                jsonBuilder.append(line);
            }
            else {
                keepReading = false;
            }
        }

        return jsonBuilder.toString();

    }

    private static void writeToSocket(String output, Socket sock) throws IOException {

        OutputStream outStream = sock.getOutputStream();
        PrintWriter writer = new PrintWriter(outStream, true);
        writer.println(output);

    }

    private static void log(String print) {
        //System.out.println(print);
    }
}
