package main.view;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Hashtable;

/**
 * A JPanel which has widgets for controlling the game of life (e.g. start or stop the game via buttons)
 */
public class ControlPanel extends JPanel {

    private final JButton startRestartBtn;
    private final JButton resetClearBtn;
    private final JButton jumpButton;
    private final JSlider delaySlider;
    private final JLabel generationTextLabel;

    public ControlPanel() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 15));

        Dimension buttonDimension = new Dimension(25, 50);

        // add start/restart button to panel
        startRestartBtn = new JButton("Start");
        startRestartBtn.setSize(buttonDimension);
        startRestartBtn.setFocusable(false);
        this.add(startRestartBtn);


        // add button for jumping to next generation
        // if the game of live is not running, only generate and display the following generation
        jumpButton = new JButton("Next generation");
        jumpButton.setSize(buttonDimension);
        jumpButton.setFocusable(false);
        this.add(jumpButton);


        // add button for clearing the field (kill all cells)
        resetClearBtn = new JButton("Clear");
        resetClearBtn.setSize(buttonDimension);
        resetClearBtn.setFocusable(false);
        this.add(resetClearBtn);


        // add JSlider for selecting the delay between creating the generations
        int maxDelay = 20;
        int minDelay = 1;
        delaySlider = new JSlider(minDelay, maxDelay, (maxDelay - minDelay) / 2);
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

    public void setResetClearBtnText(String text) {
        resetClearBtn.setText(text);
    }

    public void setGenerationTextLabel(int generationCounter) {
        generationTextLabel.setText("Generation: " + generationCounter);
    }


    /**
     * methods for adding ActionListener to ui elements
     */

    public void addStartRestartBtnActionListener(ActionListener a) {
        startRestartBtn.addActionListener(a);
    }

    public void addResetClearBtnActionListener(ActionListener a) {
        resetClearBtn.addActionListener(a);
    }

    public void addJumpButtonActionListener(ActionListener a) {
        jumpButton.addActionListener(a);
    }

    public void addDelaySliderChangeListener(ChangeListener a) {
        delaySlider.addChangeListener(a);
    }
}


















