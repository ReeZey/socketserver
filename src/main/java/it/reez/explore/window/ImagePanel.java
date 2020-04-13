package it.reez.explore.window;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import static java.awt.RenderingHints.*;

class ImagePanel extends JPanel
{
    BufferedImage image;
    double scale;

    public ImagePanel()
    {
        loadImage();
        scale = 1.0;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(KEY_INTERPOLATION,
                VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        int w = getWidth();
        int h = getHeight();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        double x = (w - scale * imageWidth)/2;
        double y = (h - scale * imageHeight)/2;
        AffineTransform at = AffineTransform.getTranslateInstance(x,y);
        at.scale(scale, scale);
        g2.drawRenderedImage(image, at);
    }

    /**
     * For the scroll pane.
     */
    public Dimension getPreferredSize()
    {
        int w = (int)(scale * image.getWidth());
        int h = (int)(scale * image.getHeight());
        return new Dimension(w, h);
    }

    public void setScale(double s)
    {
        scale = s;
        revalidate();
        repaint();
    }

    private void loadImage()
    {
        String fileName = "./map/0x0.png";
        try
        {
            image = ImageIO.read(new File(fileName));
        }
        catch(MalformedURLException mue)
        {
            System.out.println("URL trouble: " + mue.getMessage());
        }
        catch(IOException ioe)
        {
            System.out.println("read trouble: " + ioe.getMessage());
        }
    }
}
