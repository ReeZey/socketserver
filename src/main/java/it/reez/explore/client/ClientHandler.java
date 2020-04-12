package it.reez.explore.client;

import it.reez.explore.io.Players;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

import static it.reez.explore.Main.players;
import static it.reez.explore.Values.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ClientHandler implements Runnable {
    private final String clientName;
    private final String clientPassword;
    private final String ip;
    private final Socket s;
    private Thread t;
    private PrintWriter writer;

    public ClientHandler(Socket s, String id) {
        this.s = s;
        this.ip = s.getInetAddress().getHostAddress();

        String[] split = id.split(",",2);
        this.clientName = split[0];
        this.clientPassword = split[1];
    }

    public void run() {
        System.out.println("Client " + clientName + " connected!");
        writer.println("Connected.");

        boolean dead = false;
        while(!dead){
            try {
                while (true){
                    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), UTF_8));
                    char[] buff = new char[8];
                    in.mark(8);
                    in.read(buff, 0, 8);
                    in.reset();

                    if(Arrays.equals(buff, new char[4])){
                        dead = true;
                        break;
                    }
                    String text = new String(buff);
                    System.out.println(clientName + ": " + text);

                    Rover r = Rover.get(clientName);
                    for(char cmd : text.toCharArray()){
                        switch(Character.toString(cmd)){
                            case FORWARD:
                                r.forward();
                                break;
                            case TURN_RIGHT:
                                r.rRight();
                                break;
                            case TURN_LEFT:
                                r.rLeft();
                                break;
                            case POSITION:
                                writer.println(r.getPos());
                                break;
                            case SCAN:
                                writer.println(r.scan());
                                break;
                        }
                    }
                }
            } catch (SocketException e){
                e.getMessage();
                dead = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Client " + clientName + " disconnected.");
        Players.save();
    }

    public void start () {
        try {
            writer = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Incomming connection");

        if(players.get(clientName) != null){
            if (t == null && players.get(clientName).getPassword().equals(clientPassword)){
                t = new Thread (this, clientName);
                t.start();
            }else{
                signInError("wrong credentials");
            }
        }else{
            signInError("not found");
        }
    }

    private void signInError(String reason){
        System.err.println(reason + " [" + clientName +":"+ clientPassword + "]");
        writer.println(reason);
    }
}
