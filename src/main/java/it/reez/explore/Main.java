package it.reez.explore;

import com.google.gson.Gson;
import it.reez.explore.client.ClientHandler;
import it.reez.explore.client.Rover;
import it.reez.explore.client.RoverPos;
import it.reez.explore.io.Players;
import it.reez.explore.io.World;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main extends JFrame {
    public final static int mapWidth = 100;
    public final static int mapHeight = 100;
    public final static int seed = 2147483646;
    static Map<String, Rover> players = new HashMap<>();
    public final static Gson gson = new Gson();
    final static int port = 1234;

    public static void main (String[] args) {
        World.generate(seed, 0,0);
        players = Players.load();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (!serverSocket.isClosed()) {
                Socket s = serverSocket.accept();
                InetAddress line = s.getInetAddress();
                if(line.toString().equals("/127.0.0.1")){
                    StringBuilder html = new StringBuilder();
                    html.append("{\n");
                    int size = players.size();
                    for(Map.Entry<String, Rover> rov : players.entrySet()) {
                        String name = rov.getKey();
                        RoverPos rp = new RoverPos(Rover.get(name));
                        if(rov.getValue().getOnline()){
                            html.append(gson.toJson(name));
                            html.append(":");
                            html.append(gson.toJson(rp));
                            if(size > 1){
                                html.append(",");
                                size--;
                            }
                            html.append("\n");
                        }else{
                            size--;
                        }
                    }
                    html.append("}");

                    //send it
                    OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
                    out.write("HTTP/1.1 200 OK\r\n");
                    out.write("Content-Type: application/json\r\n");
                    out.write("Access-Control-Allow-Origin: *\r\n");
                    out.write("\r\n");
                    out.write(html.toString());
                    out.flush();
                    s.close();
                }else {
                    byte[] buff = new byte[32];
                    InputStream input = s.getInputStream();
                    input.read(buff, 0, buff.length);
                    String id = new String(buff, UTF_8).trim();

                    ClientHandler c = new ClientHandler(s, id);
                    c.start();
                }
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
