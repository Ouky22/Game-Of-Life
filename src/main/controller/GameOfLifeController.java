package main.controller;

import main.model.GameOfLife;
import main.model.GameOfLifeField;
import main.view.ControlPanel;
import main.view.FieldPanel;
import main.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Mediator between the logic and the UI
 */
public class GameOfLifeController {
    private final MainFrame mainFrame;
    private final FieldPanel fieldPanel;
    private final ControlPanel controlPanel;

    private final GameOfLife gameOfLife;

    private int delay = 1000;
    private final Timer timer;


    public GameOfLifeController(GameOfLife gof, MainFrame frame) {
        this.gameOfLife = gof;
        this.mainFrame = frame;
        this.fieldPanel = frame.getFieldPanel();
        this.controlPanel = frame.getControlPanel();

        // init ui with values from gameOfLifeField and add actionListener
        init();

        // initialize timer
        this.timer = new Timer(delay, (e) -> {
            gameOfLife.loadNextGeneration();
            controlPanel.setResetClearBtnText("Reset");
        });
        timer.setInitialDelay(50);
    }

    private void init() {
        // --- initialize fieldPanel with values from gameOfLifeField and pass ActionListener for buttons
        fieldPanel.addButtonActionListener((e) -> {
            if (e.getSource() instanceof JButton btn) {
                boolean alive = btn.getActionCommand().split(",")[0].equals("alive");
                int row = Integer.parseInt(btn.getActionCommand().split(",")[1]);
                int column = Integer.parseInt(btn.getActionCommand().split(",")[2]);
                gameOfLife.setCellAt(row, column, !alive);
            }
        });

        // --- set ActionListener of ControlPanel
        // start/restart button
        controlPanel.addStartRestartBtnActionListener((e) -> {
            if (e.getSource() instanceof JButton startRestartButton)
                if (isGameOfLiveRunning()) {
                    stopGameOfLive();
                    startRestartButton.setText("Start");
                } else {
                    startGameOfLive();
                    startRestartButton.setText("Stop");
                }
        });

        // jump button
        controlPanel.addJumpButtonActionListener((e) -> triggerNextGeneration());

        // reset/clear button
        controlPanel.addResetClearBtnActionListener((e) -> {
            if (e.getSource() instanceof JButton resetClearButton)
                // if the current generation is not the first generation...
                if (gameOfLife.getGenerationCounter() > 1) {
                    //... reset to the first generation
                    gameOfLife.resetToFirstGeneration();
                    // update text of resetClearButton because now its functionality is to clear the field
                    resetClearButton.setText("Clear");
                    // update generationTextLabel
                } else {
                    // ...otherwise reset the game of life
                    gameOfLife.resetGameOfLife();
                }
        });

        // delay slider
        controlPanel.addDelaySliderChangeListener((e) -> {
            if (e.getSource() instanceof JSlider delaySlider) {
                int currentSpeed = delay;
                int currentValue = delaySlider.getValue();
                // only change speed when the slider has a final result
                // or if the difference between current slider value and speed is greater than 3
                if (delaySlider.getValueIsAdjusting() || Math.abs(currentValue - currentSpeed) > 3)
                    setDelay((delaySlider.getMaximum() - currentValue) * 100);
            }
        });

        mainFrame.setVisible(true);
    }

    /**
     * Starts the timer so that it is firing events periodically with the given delay causing to load the next generation
     */
    public void startGameOfLive() {
        timer.setRepeats(true);
        timer.restart();
    }

    /**
     * Stopps the timer so that it does not fire events causing to load the next generation
     */
    public void stopGameOfLive() {
        timer.stop();
    }

    /**
     * Triggers timer so the next generation will be generated and drawn.
     * If the timer is running before this method call, it will continue afterwards.
     * If the timer is not running before this method call,
     * it will only trigger the timer once and then stops again.
     */
    public void triggerNextGeneration() {
        // if the timer is not running, the timer should fire only once (until it is started again)
        if (!timer.isRunning())
            timer.setRepeats(false);

        // restart timer so that the next event is fired after the initial delay of timer
        // causing the next generation to be created and displayed
        timer.restart();
    }

    public boolean isGameOfLiveRunning() {
        return timer.isRunning();
    }

    /**
     * Sets the delay between each Generation
     *
     * @param delay in milliseconds
     */
    public void setDelay(int delay) {
        this.delay = delay;
        this.timer.setDelay(delay);
    }
}
