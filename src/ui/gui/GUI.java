package ui.gui;

import domain.GameOfLiveManager;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    private final int FIELD_WIDTH = 50;
    private final int FIELD_HEIGHT = 50;

    private final FieldPanel fieldPanel;

    private final GameOfLiveManager gameOfLiveManager = new GameOfLiveManager(FIELD_HEIGHT, FIELD_WIDTH);

    private Timer timer;

    public GUI() {
        // create UI
        this.setLayout(new BorderLayout());

        JButton startBtn = new JButton();
        startBtn.setSize(new Dimension(25, 50));
        startBtn.setText("Start");
        startBtn.setFocusable(false);
        startBtn.addActionListener((e) -> {
            if (timer.isRunning()) timer.stop();
            else timer.restart();
        });
        this.add(startBtn, BorderLayout.NORTH);

        fieldPanel = new FieldPanel(gameOfLiveManager);
        this.add(fieldPanel, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(750, 750);
        this.setVisible(true);
        

        // set Timer
        timer = new Timer(gameOfLiveManager.getSpeed(), (e) -> {
            for (int[] coordinate : gameOfLiveManager.loadNextGeneration())
                fieldPanel.toggleButton(coordinate);
        });
    }
}
