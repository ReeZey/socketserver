package it.reez.socketserver;

import java.io.*;
import java.net.*;

import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {
    public static void main(String[] args) {

        int port = 1234;

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            Mars.load();
            Map<String, Rover> players = new HashMap<>();
            players.put("5010", new Rover(24, 4, 2, "Red"));
            players.put("5020", new Rover(24, 6, 2, "Blue"));
            players.put("5030", new Rover(24, 6, 2, "Purple"));

            while (true) {
                Socket socket = server.accept();

                InetAddress line = socket.getInetAddress();
                if(line.toString().equals("/127.0.0.1")){
                    StringBuilder html = new StringBuilder();
                    for(Map.Entry<String, Rover> rov : players.entrySet()) {
                        Rover r = rov.getValue();
                        String pos = "\""+rov.getKey()+"\":{\"x\":\""+r.getX() + "\", \"y\": \"" + r.getY()+"\", \"c\":\""+r.getC()+"\"},";
                        html.append(pos).append("\n");
                    }
                    String text = html.toString();
                    text = text.substring(0,text.length()-2)+"\n";
                    OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
                    out.write("HTTP/1.1 200 OK\r\n");
                    out.write("Content-Type: application/json\r\n");
                    out.write("\r\n");
                    out.write("{\n"+text+"}");
                    out.flush();
                    socket.close();
                }else{
                    OutputStream output = socket.getOutputStream();

                    PrintWriter writer = new PrintWriter(output, true);

                    writer.println("Connected");

                    Thread Client = new Thread(() -> {
                        boolean dead = false;
                        char[] preview = new char[8];
                        BufferedReader in = null;

                        while (!dead) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
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
                                                    break;
                                                case "r":
                                                    r.rRight();
                                                    break;
                                                case "l":
                                                    r.rLeft();
                                                    break;
                                                case "p":
                                                    writer.println("Position x:" + r.getX() + " y:" + r.getY() + " direction:" + dir[r.getR()]);
                                                    break;
                                                case "f":
                                                    r.forward();
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
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}