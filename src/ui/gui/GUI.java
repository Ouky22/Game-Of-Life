package ui.gui;

import domain.GameOfLiveManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    private final int FIELD_WIDTH = 50;
    private final int FIELD_HEIGHT = 50;

    private final FieldPanel fieldPanel;

    private final GameOfLiveManager gameOfLiveManager
            = new GameOfLiveManager(FIELD_HEIGHT, FIELD_WIDTH, new TimerListener());


    public GUI() {
        this.setLayout(new BorderLayout());

        // add ControlPanel, which is for adjusting settings for the gameOfLive
        this.add(new ControlPanel(gameOfLiveManager), BorderLayout.NORTH);

        // add fieldPanel, which contains the gameOfLive field
        fieldPanel = new FieldPanel(gameOfLiveManager);
        this.add(fieldPanel, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(750, 750);
        this.setVisible(true);
    }

    class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int[] coordinate : gameOfLiveManager.loadNextGeneration())
                fieldPanel.toggleButton(coordinate);
        }
    }
}
