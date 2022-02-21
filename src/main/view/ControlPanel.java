//package main.view;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.Hashtable;
//
//public class ControlPanel extends JPanel {
//
//    private final JLabel generationTextLabel;
//    private final JButton resetClearButton;
//
//    public ControlPanel() {
//        this.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 15));
//
//        Dimension buttonDimension = new Dimension(25, 50);
//        // add start/restart button to panel
//        JButton startBtn = new JButton("Start");
//        startBtn.setSize(buttonDimension);
//        startBtn.setFocusable(false);
//        startBtn.addActionListener((e) -> {
//            if (gameOfLiveController.isGameOfLiveRunning()) {
//                gameOfLiveController.stopGameOfLive();
//                startBtn.setText("Start");
//            } else {
//                gameOfLiveController.startGameOfLive();
//                startBtn.setText("Stop");
//            }
//        });
//        this.add(startBtn);
//
//
//        // add button for jumping to next generation
//        // if the game of live is not running, only generate and display the following generation
//        JButton jumpButton = new JButton("Next generation");
//        jumpButton.setSize(buttonDimension);
//        jumpButton.setFocusable(false);
//        jumpButton.addActionListener((e) -> gameOfLiveController.triggerNextGeneration());
//        this.add(jumpButton);
//
//
//        // add button for clearing the field
//        resetClearButton = new JButton("Clear");
//        resetClearButton.setSize(buttonDimension);
//        resetClearButton.setFocusable(false);
//        resetClearButton.addActionListener((e) -> {
//            // if the current generation is not the first generation...
//            if (gameOfLiveController.getGenerationCounter() > 1) {
//                //... reset to the first generation and update fieldPanel...
//                gui.toggleButtonsInFieldPanel(gameOfLiveController.resetToFirstGeneration());
//                resetClearButton.setText("Clear");
//            } else {
//                // ...otherwise kill all cells for clearing the field
//                gui.toggleButtonsInFieldPanel(gameOfLiveController.killAllCells());
//            }
//
//            updateGenerationTextLabel(gameOfLiveController.getGenerationCounter());
//        });
//        this.add(resetClearButton);
//
//
//        // add JSlider for selecting the delay between creating the generations
//        int maxDelay = 20;
//        int minDelay = 1;
//        JSlider delaySlider = new JSlider(minDelay, maxDelay, (maxDelay - minDelay) / 2);
//        delaySlider.addChangeListener((e) -> {
//            int currentSpeed = gameOfLiveController.getDelay();
//            int currentValue = delaySlider.getValue();
//            // only change speed when the slider has a final result
//            // or if the difference between current slider value and speed is greater than 3
//            if (delaySlider.getValueIsAdjusting() || Math.abs(currentValue - currentSpeed) > 3)
//                gameOfLiveController.setDelay((maxDelay - currentValue) * 100);
//        });
//        // Hashtable which contains labels for min and max value of slider
//        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
//        labelTable.put(minDelay, new JLabel("slow"));
//        labelTable.put(maxDelay, new JLabel("fast"));
//        delaySlider.setLabelTable(labelTable);
//        delaySlider.setPaintLabels(true);
//        this.add(delaySlider);
//
//
//        // add textField for displaying the generation counter
//        this.generationTextLabel = new JLabel("Generation: 1");
//        this.add(generationTextLabel);
//    }
//
//    public void updateGenerationTextLabel(int generationCounter) {
//        generationTextLabel.setText("Generation: " + generationCounter);
//
//        if (generationCounter > 1)
//            resetClearButton.setText("Reset");
//    }
//}
