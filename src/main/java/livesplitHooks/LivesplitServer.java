package livesplitHooks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import necesse.engine.GameLog;

public class LivesplitServer {
    private static class Command {
        private final String cmdString;

        private Command(String cmdString) {
            this.cmdString = cmdString;
        }
    }

    public static final Command START_TIMER = new Command("starttimer");
    public static final Command PAUSE_GAMETIME = new Command("pausegametime");
    public static final Command UNPAUSE_GAMETIME = new Command("unpausegametime");
    public static final Command SPLIT = new Command("split");
    public static final Command RESET = new Command("reset");

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public LivesplitServer() {
        socket = new Socket();
    }

    public void connect() {
        if (isConnected()) {
            return;
        }
        try {
            socket = new Socket("localhost", LivesplitHooksEntry.config.livesplitPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            GameLog.out.println("Connected to LiveSplit server");
        } catch (IOException e) {
            GameLog.warn.println("Could not connect to LiveSplit server");
        }
    }

    public void sendCommand(Command command) {
        if (!isConnected()) {
            return;
        }
        out.println(command.cmdString);
    }

    public void disconnect() {
        if (out != null) {
            out.close();
            out = null;
        }
        try {
            socket.close();
        } catch (IOException e) {
        }
    }

    public boolean isConnected() {
        if (!socket.isConnected() || out == null) {
            return false;
        }
        try {
            out.println("getcurrenttime");
            in.readLine();
        } catch (IOException e) {
            disconnect();
            return false;
        }
        return socket.isConnected();
    }
}
