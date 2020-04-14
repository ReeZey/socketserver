package it.reez.explore;

import com.google.gson.Gson;
import it.reez.explore.client.ClientHandler;
import it.reez.explore.client.Rover;
import it.reez.explore.io.Players;
import it.reez.explore.io.World;
import it.reez.explore.noise.Noise;
import it.reez.explore.window.Window;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main extends JFrame {
    public final static int mapWidth = 100;
    public final static int mapHeight = 100;
    public final static int seed = 69;
    static Map<String, Rover> players = new HashMap<>();
    public final static Gson gson = new Gson();
    final static int port = 1234;
    public static Window win;

    public static void main (String[] args) {

        World.generate(Noise.generateSimplexNoise(seed, 0, 0));

        players = Players.load();

        //win = new Window();
        //win.updateTitle();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            while (!serverSocket.isClosed()) {
                Socket s = serverSocket.accept();

                byte[] buff = new byte[32];
                InputStream input = s.getInputStream();
                input.read(buff, 0, buff.length);
                String id = new String(buff, UTF_8).trim();

                ClientHandler c = new ClientHandler(s, id);
                c.start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static Map<String, Rover> getPlayers() {
        return players;
    }
}
