package main.view;

import main.model.GameOfLife;
import main.view.controlpanel.BottomControlPanel;
import main.view.controlpanel.TopControlPanel;

import javax.swing.*;
import java.awt.*;

/**
 * JFrame which contains all elements for the graphical representation of the game of life
 */
public class MainFrame extends JFrame {
    private final FieldPanel fieldPanel;
    private final TopControlPanel topControlPanel;
    private final BottomControlPanel bottomControlPanel;

    public MainFrame(GameOfLife gameOfLife) {
        this.setLayout(new BorderLayout());

        // add fieldPanel, which displays the gameOfLive field
        this.add(fieldPanel = new FieldPanel(gameOfLife), BorderLayout.CENTER);

        // add topControlPanel, which is for adjusting the game of life
        this.add(topControlPanel = new TopControlPanel(gameOfLife), BorderLayout.NORTH);

        // add bottomControlPanel, which is for adjusting the game of life
        this.add(bottomControlPanel = new BottomControlPanel(), BorderLayout.SOUTH);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(750, 750);
        this.setVisible(true);
    }

    public FieldPanel getFieldPanel() {
        return fieldPanel;
    }

    public TopControlPanel getTopControlPanel() {
        return topControlPanel;
    }

    public BottomControlPanel getBottomControlPanel() {
        return bottomControlPanel;
    }
}
