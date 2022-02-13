package ui.gui;

import domain.GameOfLiveManager;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class ControlPanel extends JPanel {
    public ControlPanel(GameOfLiveManager gameOfLiveManager) {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 15));

        Dimension buttonDimension = new Dimension(25, 50);
        // add start/restart button to panel
        JButton startBtn = new JButton("Start");
        startBtn.setSize(buttonDimension);
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


        // add button for jumping to next generation
        // if the game of live is not running, only generate and display the following generation
        JButton jumpButton = new JButton("Next generation");
        jumpButton.setSize(buttonDimension);
        jumpButton.setFocusable(false);
        jumpButton.addActionListener((e) -> gameOfLiveManager.triggerNextGeneration());
        this.add(jumpButton);


        // add JSlider for selecting the delay between creating the generations
        int maxDelay = 20;
        int minDelay = 1;
        JSlider delaySlider = new JSlider(minDelay, maxDelay, (maxDelay - minDelay) / 2);
        delaySlider.addChangeListener((e) -> {
            int currentSpeed = gameOfLiveManager.getDelay();
            int currentValue = delaySlider.getValue();
            // only change speed when the slider has a final result
            // or if the difference between current slider value and speed is greater than 3
            if (delaySlider.getValueIsAdjusting() || Math.abs(currentValue - currentSpeed) > 3)
                gameOfLiveManager.setDelay((maxDelay - currentValue) * 100);
        });
        // Hashtable which contains labels for min and max value of slider
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(minDelay, new JLabel("slow"));
        labelTable.put(maxDelay, new JLabel("fast"));
        delaySlider.setLabelTable(labelTable);
        delaySlider.setPaintLabels(true);
        this.add(delaySlider);


    }
}
