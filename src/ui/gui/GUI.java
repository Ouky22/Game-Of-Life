package ui.gui;

import domain.GameOfLiveField;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    private final int FIELD_WIDTH = 50;
    private final int FIELD_HEIGHT = 50;

    private final FieldPanel fieldPanel;

    GameOfLiveField gameOfLiveField = new GameOfLiveField(FIELD_HEIGHT, FIELD_WIDTH);
    boolean gameOfLiveRunning = false;

    public GUI() {
        this.setLayout(new BorderLayout());

        fieldPanel = new FieldPanel(gameOfLiveField);
        this.add(fieldPanel, BorderLayout.CENTER);

        JButton startBtn = new JButton();
        startBtn.setSize(new Dimension(25, 50));
        startBtn.setText("Start");
        startBtn.setFocusable(false);
        startBtn.addActionListener((e) -> {
            gameOfLiveRunning = !gameOfLiveRunning;
            System.out.println("Start clicked");
        });
        this.add(startBtn, BorderLayout.NORTH);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(750, 750);
        this.setVisible(true);

        startGameOfLive();
    }

    private void startGameOfLive() {
        while (true) {
            while (gameOfLiveRunning) {
                for (int[] coordinate : gameOfLiveField.loadNextGeneration())
                    fieldPanel.toggleButton(coordinate);

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
