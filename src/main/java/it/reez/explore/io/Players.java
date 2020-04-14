package it.reez.explore.io;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.reez.explore.Main;
import it.reez.explore.client.Rover;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;

import static it.reez.explore.Main.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Players {
    static Map<String, Rover> players;
    public static Map<String, Rover> load() {
        players = Main.getPlayers();


        File file = new File("players.json");

        System.out.println("Trying to load players...");
        if(file.exists()) {
            try{
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                fis.close();
                String str = new String(data, UTF_8);
                Type mapType = new TypeToken<Map<String, Rover>>(){}.getType();
                players = new Gson().fromJson(str, mapType);
                System.out.println("Players loaded successfully");
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            try{
                System.err.println("Players file not found... \nGenerating new players");
                players.put("test", new Rover("password"));
                players.put("ris", new Rover("fis"));
                Writer wr = new OutputStreamWriter(new FileOutputStream("players.json"), UTF_8);
                wr.write(gson.toJson(players));
                wr.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return players;
    }

    public static void save() {
        System.out.print("Saving players...");
        players = Main.getPlayers();
        try {
            Writer wr = new OutputStreamWriter(new FileOutputStream("players.json"), UTF_8);
            wr.write(gson.toJson(players));
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getOnline() {
        players = Main.getPlayers();
        int online = 0;
        for(Rover r : players.values()){
            if(r.getOnline()){
                online++;
            }
        }
        return online;
    }
}
