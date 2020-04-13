package it.reez.explore.io;

import it.reez.explore.noise.NoiseClass;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static it.reez.explore.Main.*;
import static it.reez.explore.Values.*;

public class World {
    public static void generate(NoiseClass noiseclass){
        System.out.println("Generating map...");
        float[][] noise = noiseclass.noise;
        int posy = noiseclass.posy;
        int posx = noiseclass.posx;
        BufferedImage image = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < mapHeight; y++){
            for(int x = 0; x < mapWidth; x++) {
                Color c;
                String BlockType;
                if (noise[x][y] > 0.72) {
                    c = STONE_COLOR;
                    BlockType = STONE;
                }else if (noise[x][y] > 0.65) {
                    String o = String.valueOf(noise[x][y]);
                    if(o.length() > 3){
                        if(Integer.parseInt(o.substring(o.length()-2)) > 97){
                            c = IRON_COLOR;
                            BlockType = IRON;
                        }else{
                            c = ROCK_COLOR;
                            BlockType = ROCK;
                        }
                    }else{
                        c = ROCK_COLOR;
                        BlockType = ROCK;
                    }
                }else if (noise[x][y] > 0.4) {
                    c = GRASS_COLOR;
                    BlockType = GRASS;
                }else if (noise[x][y] > 0.3) {
                    c = SAND_COLOR;
                    BlockType = SAND;
                }else {
                    String o = String.valueOf(noise[x][y]);
                    if(Integer.parseInt(o.substring(o.length()-1)) > 2){
                        c = WATER_COLOR.brighter();
                    }else{
                        c = WATER_COLOR;
                    }
                    BlockType = WATER;
                }
                image.setRGB(x, y, c.getRGB());

                map[y][x] = BlockType;
            }
            /*
            int yy = y + 1;
            for(char x : String.valueOf(yy).toCharArray()){
                System.out.print("\b");
            }
            System.out.print(yy);
             */
        }
        System.out.println("\nSaving...");
        File imageFile = new File("./map/"+posy+"x"+posx+".png");
        try {
            ImageIO.write(image, "png", imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Complete");
    }

    public static String get(int y, int x) {
        return map[y][x];
    }
}
