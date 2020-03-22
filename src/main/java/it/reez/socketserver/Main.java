package it.reez.socketserver;

import java.io.*;
import java.net.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {
    public static void main(String[] args) {


        //Socket port
        int port = 1234;

        //Trying to start server
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            //Load map
            Mars.load();
            //Load players
            Map<String, Rover> players = new HashMap<>();
            players.put("jagg", new Rover(24, 4, 0, "green"));
            players.put("drla", new Rover(26, 4, 2, "red"));
            players.put("risi", new Rover(28, 4, 2, "purple"));

            while (true) {
                //accept all requests
                Socket socket = server.accept();

                InetAddress line = socket.getInetAddress();
                if(line.toString().equals("/127.0.0.1")){
                    //Map viewer, Only accessable at localhost or 127.0.0.1
                    StringBuilder html = new StringBuilder();
                    //add all rovers to the json file
                    for(Map.Entry<String, Rover> rov : players.entrySet()) {
                        Rover r = rov.getValue();
                        String pos = "\""+rov.getKey()+"\":{\"x\":\""+r.getX() + "\", \"y\": \"" + r.getY()+"\", \"r\":\""+r.getDir()+"\", \"p\":\""+r.getP()+"\", \"c\":\""+r.getC()+"\"},";
                        html.append(pos).append("\n");
                    }
                    //format it
                    String text = html.toString();
                    text = text.substring(0,text.length()-2)+"\n";

                    //send it
                    OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
                    out.write("HTTP/1.1 200 OK\r\n");
                    out.write("Content-Type: application/json\r\n");
                    out.write("Access-Control-Allow-Origin: *\r\n");
                    out.write("\r\n");
                    out.write("{\n"+text+"}");
                    out.flush();
                    socket.close();
                }else{
                    //Print to client
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                    writer.println("Connected");

                    //Create thread to handle new users
                    Thread Client = new Thread(() -> {
                        boolean dead = false;
                        char[] preview = new char[8];
                        BufferedReader in = null;
                        //while client is still listening to socket
                        while (!dead) {
                            //manual delay to prevent spam
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //try to read whatever the client sent
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
                            //if client didn't send anything at all, kill socket.
                            if(Arrays.equals(preview, new char[8])){
                                dead = true;
                            }else{
                                //Read message
                                String s = String.valueOf((preview));

                                //try to parse rover pin
                                String rpin = s.substring(0,4);

                                //check if a plyer has that pin
                                if(players.containsKey(rpin)){
                                    //print all executions done by the client
                                    System.out.println("Rover "+ rpin + " executed " + s.substring(4,8));

                                    //add all commands to an array and fire them once each
                                    char[] commands = s.substring(4,8).toCharArray();
                                    for(char cmd : commands){
                                        if(Character.isLetter(cmd)) {
                                            //get current command
                                            String c = Character.toString(cmd);

                                            //get rover
                                            Rover r = players.get(rpin);

                                            //execute command
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
                                                    writer.println("Position x:" + r.getX() + " y:" + r.getY() + " direction:" + r.getDir());
                                                    break;
                                                case "f":
                                                    r.forward();
                                                    break;
                                                case "d":
                                                    r.dig();
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
                    //Start thread
                    Client.start();
                }
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}