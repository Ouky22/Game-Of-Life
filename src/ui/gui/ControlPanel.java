package ui.gui;

import domain.GameOfLiveManager;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    public ControlPanel(GameOfLiveManager gameOfLiveManager) {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 15));

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
        this.add(startBtn);

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


        this.add(speedSlider);
    }
}
