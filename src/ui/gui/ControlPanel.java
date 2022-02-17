package ui.gui;

import domain.GameOfLiveManager;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class ControlPanel extends JPanel {

    private JLabel generationTextLabel;

    public ControlPanel(GameOfLiveManager gameOfLiveManager, GUI gui) {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 15));

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

        
        // add button for clearing the field
        JButton clearBtn = new JButton("Clear");
        clearBtn.setSize(buttonDimension);
        clearBtn.setFocusable(false);
        clearBtn.addActionListener((e) -> {
            // if the current generation is not the first generation...
            if (gameOfLiveManager.getGenerationCounter() > 1) {
                //... reset to the first generation and update fieldPanel...
                gui.toggleButtonsInFieldPanel(gameOfLiveManager.resetToFirstGeneration());
            } else {
                // ...otherwise kill all cells for clearing the field
                gui.toggleButtonsInFieldPanel(gameOfLiveManager.killAllCells());
            }

            updateGenerationTextLabel(gameOfLiveManager.getGenerationCounter());
        });
        this.add(clearBtn);


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


        // add textField for displaying the generation counter
        this.generationTextLabel = new JLabel("Generation: 1");
        this.add(generationTextLabel);
    }

    public void updateGenerationTextLabel(int generationCounter) {
        generationTextLabel.setText("Generation: " + generationCounter);
    }
}
