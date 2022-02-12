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

        // JPanel for buttons and slider
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 50, 15));

        // add start/restart button to panel
        JButton startBtn = new JButton();
        startBtn.setSize(new Dimension(25, 50));
        startBtn.setText("Start");
        startBtn.setFocusable(false);
        startBtn.addActionListener((e) -> {
            if (gameOfLiveManager.isGameOfLiveRunning()) {
                gameOfLiveManager.stopGameOfLive();
                startBtn.setText("Start");
            } else {
                gameOfLiveManager.startGameOfLive();
                startBtn.setText("Stop");
            }
        });
        panel.add(startBtn);

        // add JSlider for speed selection to panel
        JSlider speedSlider = new JSlider();
        int maxSpeedValue = 20;
        speedSlider.setMaximum(maxSpeedValue);
        speedSlider.setMinimum(1);
        speedSlider.setValue(10);
        speedSlider.addChangeListener((e) -> {
            int currentSpeed = gameOfLiveManager.getDelay();
            int currentValue = speedSlider.getValue();
            // only change speed when the slider has a final result
            // or if the difference between current slider value and speed is greater than 3
            if (speedSlider.getValueIsAdjusting() || Math.abs(currentValue - currentSpeed) > 3)
                gameOfLiveManager.setDelay((maxSpeedValue - currentValue) * 100);
        });
        panel.add(speedSlider);
        this.add(panel, BorderLayout.NORTH);

        // add fieldPanel, which contains the field
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
