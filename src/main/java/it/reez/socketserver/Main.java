package it.reez.socketserver;

import java.io.*;
import java.net.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {
    public static void main(String[] args) {

        int port = 1234;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            Mars.load();
            Map<String, Rover> players = new HashMap<>();
            players.put("5029", new Rover(4, 4, 0));
            players.put("5025", new Rover(59, 4, 1));
            players.put("5010", new Rover(24, 4, 3));

            while (true) {
                Socket socket = serverSocket.accept();
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output);

                writer.println("Connected");
                writer.flush();

                Thread Client = new Thread(() -> {
                    boolean dead = false;
                    char[] preview = new char[8];
                    BufferedReader in = null;

                    while (!dead) {
                        try {
                            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
                            preview = new char[8];
                            in.mark(8);
                            in.read(preview, 0, 8);
                            in.reset();
                        }catch (SocketException e){
                            System.out.println("Error: "+e.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(Arrays.equals(preview, new char[8])){
                            dead = true;
                        }else{
                            //Read messages
                            String s = String.valueOf((preview));
                            String rpin = s.substring(0,4);
                            if(players.containsKey(rpin)){
                                System.out.println("Rover "+ rpin + " executed " + s.substring(4,8));
                                char[] commands = s.substring(4,8).toCharArray();

                                for(char cmd : commands){
                                    if(Character.isLetter(cmd)) {
                                        String c = Character.toString(cmd);
                                        Rover r = players.get(rpin);
                                        String[] dir = {"N", "E", "S", "W"};
                                        switch (c) {
                                            case "s":
                                                String scan = r.scan(r.getX(), r.getY(), r.getR());
                                                writer.println(scan);
                                                writer.flush();
                                                break;
                                            case "r":
                                                r.rRight();
                                                break;
                                            case "l":
                                                r.rLeft();
                                                break;
                                            case "p":
                                                writer.write("Position x:" + r.getX() + " y:" + r.getY() + " direction:" + dir[r.getR()]);
                                                writer.flush();
                                                break;
                                            case "f":
                                                r.forward();
                                                break;
                                            case "X":
                                                for(Map.Entry<String, Rover> rov : players.entrySet()) {
                                                    String rover = "Rover "+ rov.getKey();
                                                    r = rov.getValue();
                                                    String pos = " Position x:" + r.getX() + " y:" + r.getY() + " direction:" + dir[r.getR()];
                                                    System.out.println(rover + pos);
                                                }
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                Client.start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}