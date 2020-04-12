package it.reez.explore.io;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.reez.explore.client.Rover;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;

import static it.reez.explore.Main.gson;
import static it.reez.explore.Main.players;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Players {
    public static void load() {
        try{
            try{
                File file = new File("players.json");
                System.out.print("Loading players");
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                fis.close();
                String str = new String(data, UTF_8);
                Type mapType = new TypeToken<Map<String, Rover>>(){}.getType();
                players = new Gson().fromJson(str, mapType);
            }catch(FileNotFoundException notFound){
                System.err.println(notFound.getMessage());
                System.err.println("Generating new players");
                players.put("test", new Rover("password"));
                players.put("ris", new Rover("fis"));
                Writer wr = new OutputStreamWriter(new FileOutputStream("players.json"), UTF_8);
                wr.write(gson.toJson(players));
                wr.close();
            }
            System.out.println(" done");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void save() {
        System.out.print("Saving players...");
        try {
            Writer wr = new OutputStreamWriter(new FileOutputStream("players.json"), UTF_8);
            wr.write(gson.toJson(players));
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
