package it.reez.explore.window;

import it.reez.explore.io.Players;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static it.reez.explore.Main.*;

public class Window {

    JFrame f = new JFrame();
    ImagePanel panel = new ImagePanel();
    ImageZoom zoom = new ImageZoom(panel);

    public Window() {
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(zoom.getUIPanel(), "North");
        JScrollPane scroll = new JScrollPane(panel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getHorizontalScrollBar().setUnitIncrement(16);
        f.getContentPane().add(scroll);
        f.setSize(800,600);
        f.setLocation(200,200);
        f.setVisible(true);

        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("test");
            }
        });
    }

    public void updateTitle(){
        f.setTitle("World map | Players "+ Players.getOnline() +" | Size " + mapWidth +":"+ mapHeight + " Seed:" + seed);
    }
}
