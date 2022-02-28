package main.view;

import main.model.GameOfLifeField;

import javax.swing.*;
import java.awt.*;

/**
 * JFrame which contains all elements for the graphical representation of the game of life
 */
public class MainFrame extends JFrame {
    private final FieldPanel fieldPanel;
    private final ControlPanel controlPanel;

    public MainFrame(GameOfLifeField gameOfLifeField) {
        this.setLayout(new BorderLayout());

        // add fieldPanel, which displays the gameOfLive field
        this.add(fieldPanel = new FieldPanel(gameOfLifeField), BorderLayout.CENTER);

        // add ControlPanel, which is for adjusting settings of the game of life
        this.add(controlPanel = new ControlPanel(gameOfLifeField), BorderLayout.NORTH);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(750, 750);
        this.setVisible(true);
    }

    public FieldPanel getFieldPanel() {
        return fieldPanel;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

}
