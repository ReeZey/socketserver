package it.reez.explore.window;

import javax.swing.*;
import java.awt.*;

class ImageZoom
{
    ImagePanel imagePanel;

    public ImageZoom(ImagePanel ip)
    {
        imagePanel = ip;
    }

    public JPanel getUIPanel()
    {
        SpinnerNumberModel model = new SpinnerNumberModel(1.0, 1, 200, 1);
        final JSpinner spinner = new JSpinner(model);
        spinner.setPreferredSize(new Dimension(45, spinner.getPreferredSize().height));
        spinner.addChangeListener(e -> {
            float scale = ((Double)spinner.getValue()).floatValue();
            imagePanel.setScale(scale);
        });
        JPanel panel = new JPanel();
        panel.add(new JLabel("scroller"));
        panel.add(spinner);
        return panel;
    }
}
