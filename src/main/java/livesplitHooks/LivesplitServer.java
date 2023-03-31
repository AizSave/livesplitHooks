package livesplitHooks;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class LivesplitServer {
    private Socket socket;
    private PrintWriter out;

    public LivesplitServer() {
        socket = new Socket();
    }

    public void connect() {
        try {
            socket.connect(new InetSocketAddress("localhost", 16834));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Could not connect to LiveSplit Server");
        }
    }

    public void sendCommand(String command) {
        if (!isConnected()) {
            System.out.println("Not connected!");
            return;
        }
        out.println(command);
    }

    public void disconnect() {
        if (out != null) {
            out.close();
        }
        try {
            socket.close();
        } catch (IOException e) {
        }
    }

    public boolean isConnected() {
        return socket.isConnected();
    }
}
