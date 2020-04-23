package it.reez.explore.io;

import it.reez.explore.noise.GenerateNoise;
import it.reez.explore.noise.Noise;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static it.reez.explore.Main.*;
import static it.reez.explore.Values.*;

public class World {
    public static void generate(int seed, int posy, int posx){

        Noise n = GenerateNoise.generateSimplexNoise(seed, posy, posx);
        float[][] noise = n.noise;

        System.out.println("Generating "+ posy + "x" + posx);
        BufferedImage image = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < mapHeight; y++){
            for(int x = 0; x < mapWidth; x++) {
                Color c;
                if (noise[x][y] > 0.72) {
                    c = STONE_COLOR;
                }else if (noise[x][y] > 0.65) {
                    String o = String.valueOf(noise[x][y]);
                    if(o.length() > 3){
                        if(Integer.parseInt(o.substring(o.length()-2)) > 97){
                            c = IRON_COLOR;
                        }else{
                            c = ROCK_COLOR;
                        }
                    }else{
                        c = ROCK_COLOR;
                    }
                }else if (noise[x][y] > 0.4) {
                    c = GRASS_COLOR;
                }else if (noise[x][y] > 0.3) {
                    c = SAND_COLOR;
                }else {
                    String o = String.valueOf(noise[x][y]);
                    if(Integer.parseInt(o.substring(o.length()-1)) > 2){
                        c = WATER_COLOR.brighter();
                    }else{
                        c = WATER_COLOR;
                    }
                }
                image.setRGB(x, y, c.getRGB());
            }
        }
        File imageFile = getMapFile(posy, posx);
        try {
            ImageIO.write(image, "png", imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(int y, int x) {
        int mapy = Math.floorDiv(y, mapHeight), mapx = Math.floorDiv(x, mapWidth);

        File f = getMapFile(mapy, mapx);
        if(!f.exists()){
            generate(seed, mapy, mapx);
        }

        BufferedImage i = null;
        try {
            i = ImageIO.read(getMapFile(mapy, mapx));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //cred till alfred
        while(y < 0){
            y += mapHeight;
        }
        while(x < 0){
            x += mapWidth;
        }
        while(y > 99){
            y -= mapHeight;
        }
        while(x > 99){
            x -= mapWidth;
        }

        int t = GRASS_COLOR.getRGB();
        try{
            t = i.getRGB(x, y);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        String block = GRASS;
        if (t == STONE_COLOR.getRGB()) {
            block = STONE;
        } else if (t == ROCK_COLOR.getRGB()) {
            block = ROCK;
        } else if (t == IRON_COLOR.getRGB()) {
            block = IRON;
        } else if (t == GRASS_COLOR.getRGB()) {
            block = GRASS;
        } else if (t == SAND_COLOR.getRGB()) {
            block = SAND;
        } else if (t == WATER_COLOR.getRGB() || t == WATER_COLOR.brighter().getRGB()) {
            block = WATER;
        }
        return block;
    }

    public static File getMapFile(int mapy, int mapx){
        return new File("./map/" + mapy + "x" + mapx + ".png");
    }
}
