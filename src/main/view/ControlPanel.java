package main.view;

import main.utility.IconProvider;
import main.model.GameOfLife;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Hashtable;

/**
 * A JPanel which has widgets for controlling the game of life (e.g. start or stop the game via buttons)
 */
public class ControlPanel extends JPanel implements Observer {

    private final JButton previousGenerationButton;
    private final JButton nextGenerationBtn;
    private final JButton startRestartBtn;
    private final JButton resetClearBtn;
    private final JSlider delaySlider;
    private final JLabel generationTextLabel;
    private final JLabel coverageTextLabel;
    private final JTextField goToTextField;

    private final GameOfLife gameOfLife;

    public ControlPanel(GameOfLife gameOfLifeField) {
        this.gameOfLife = gameOfLifeField;
        gameOfLifeField.register(this);

        this.setLayout(new FlowLayout(FlowLayout.CENTER, 32, 12));

        // create JPanel containing the control buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        // create button for loading previous generation
        previousGenerationButton = new JButton(IconProvider.getIcon(IconProvider.Icon.PREVIOUS));
        previousGenerationButton.setFocusable(false);
        previousGenerationButton.setToolTipText("Go to previous generation");
        previousGenerationButton.setBorder(BorderFactory.createEmptyBorder());
        previousGenerationButton.setContentAreaFilled(false);

        // create start/restart button
        startRestartBtn = new JButton(IconProvider.getIcon(IconProvider.Icon.START));
        startRestartBtn.setFocusable(false);
        startRestartBtn.setToolTipText("Start the game of life");
        startRestartBtn.setBorder(BorderFactory.createEmptyBorder());
        startRestartBtn.setContentAreaFilled(false);

        // create button for loading next generation
        nextGenerationBtn = new JButton(IconProvider.getIcon(IconProvider.Icon.NEXT));
        nextGenerationBtn.setFocusable(false);
        nextGenerationBtn.setToolTipText("Go to next generation");
        nextGenerationBtn.setBorder(BorderFactory.createEmptyBorder());
        nextGenerationBtn.setContentAreaFilled(false);

        // create button for clearing or resetting the field (kill all cells)
        resetClearBtn = new JButton(IconProvider.getIcon(IconProvider.Icon.CLEAR));
        resetClearBtn.setFocusable(false);
        resetClearBtn.setToolTipText("Clear the field");
        resetClearBtn.setBorder(BorderFactory.createEmptyBorder());
        resetClearBtn.setContentAreaFilled(false);

        // add buttons to JPanel and add JPanel to this ControlPanel
        btnPanel.add(previousGenerationButton);
        btnPanel.add(startRestartBtn);
        btnPanel.add(nextGenerationBtn);
        btnPanel.add(resetClearBtn);
        this.add(btnPanel);


        // add JSlider for selecting the delay between creating the generations
        int maxDelay = 10;
        int minDelay = 1;
        delaySlider = new JSlider(minDelay, maxDelay, (maxDelay - minDelay) / 2);
        // Hashtable which contains labels for min and max value of slider
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(minDelay, new JLabel("slow"));
        labelTable.put(maxDelay, new JLabel("fast"));
        delaySlider.setLabelTable(labelTable);
        delaySlider.setPaintLabels(true);
        this.add(delaySlider);


        // add JPanel, which contains JLabel and JTextField for "go to" functionality
        JPanel goToPanel = new JPanel();
        JLabel goToLabel = new JLabel("Go to: ");
        goToLabel.setToolTipText("Go to certain generation");
        goToPanel.add(goToLabel);
        goToPanel.add(goToTextField = new JTextField("", 3));
        this.add(goToPanel);


        // add JPanel, which contains generation and coverage JTextLabel
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        generationTextLabel = new JLabel("Generation: 1");
        coverageTextLabel = new JLabel("Coverage: 000%");
        infoPanel.add(generationTextLabel);
        infoPanel.add(coverageTextLabel);
        this.add(infoPanel);
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

    public void addNextGenerationBtnActionListener(ActionListener a) {
        nextGenerationBtn.addActionListener(a);
    }

    public void addDelaySliderChangeListener(ChangeListener a) {
        delaySlider.addChangeListener(a);
    }

    @Override
    public void update() {
        generationTextLabel.setText("Generation: " + gameOfLife.getGenerationCounter());

        if (gameOfLife.getGenerationCounter() > 1) {
            resetClearBtn.setIcon(IconProvider.getIcon(IconProvider.Icon.RESET));
            resetClearBtn.setToolTipText("Reset to first generation");
        } else {
            resetClearBtn.setIcon(IconProvider.getIcon(IconProvider.Icon.CLEAR));
            resetClearBtn.setToolTipText("Clear the field");
        }
    }
}


















