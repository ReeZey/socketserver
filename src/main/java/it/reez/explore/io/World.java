package it.reez.explore.io;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static it.reez.explore.Main.*;
import static it.reez.explore.Values.*;

public class World {
    public static void generate(){
        System.out.println("Generating map...");
        BufferedImage image = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < mapHeight; y++){
            for(int x = 0; x < mapWidth; x++) {
                Color c;
                String BlockType;
                if (noise[x][y] > 0.72) {
                    c = new Color(92, 92, 92);
                    BlockType = STONE;
                }else if (noise[x][y] > 0.65) {
                    String o = String.valueOf(noise[x][y]);
                    if(o.length() > 3){
                        if(Integer.parseInt(o.substring(o.length()-2)) > 97){
                            c = new Color(15, 208, 193, 255);
                            BlockType = DIAMOND;
                        }else{
                            c = new Color(128, 128, 128);
                            BlockType = ROCK;
                        }
                    }else{
                        c = new Color(128, 128, 128);
                        BlockType = ROCK;
                    }
                }else if (noise[x][y] > 0.4) {
                    c = new Color(102, 180, 17, 255);
                    BlockType = GRASS;
                }else if (noise[x][y] > 0.3) {
                    c = new Color(197, 201, 25);
                    BlockType = SAND;
                }else {
                    c = new Color(0, 0, 255);
                    BlockType = WATER;
                }
                image.setRGB(x, y, c.getRGB());
                map[y][x] = BlockType;
            }
            int yy = y + 1;
            for(char x : String.valueOf(yy).toCharArray()){
                System.out.print("\b");
            }
            System.out.print(yy);
        }
        System.out.println("\nSaving...");
        File imageFile = new File("map.png");
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
