package main.controller;

import main.model.GameOfLife;
import main.utility.IconProvider;
import main.view.controlpanel.BottomControlPanel;
import main.view.controlpanel.TopControlPanel;
import main.view.FieldPanel;
import main.view.MainFrame;

import javax.swing.*;

/**
 * Mediator between the logic and the UI
 */
public class GameOfLifeController {
    private final MainFrame mainFrame;
    private final FieldPanel fieldPanel;
    private final TopControlPanel topControlPanel;
    private final BottomControlPanel bottomControlPanel;

    private final GameOfLife gameOfLife;

    private int delay = 1000;
    private final Timer timer;


    public GameOfLifeController(GameOfLife gof, MainFrame frame) {
        this.gameOfLife = gof;
        this.mainFrame = frame;
        this.fieldPanel = frame.getFieldPanel();
        this.topControlPanel = frame.getTopControlPanel();
        this.bottomControlPanel = frame.getBottomControlPanel();

        // init ui with values from gameOfLifeField and add actionListener
        init();

        // initialize timer
        this.timer = new Timer(delay, (e) -> gameOfLife.loadNextGeneration());
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
        topControlPanel.addStartRestartBtnActionListener((e) -> {
            if (e.getSource() instanceof JButton startRestartButton)
                if (isGameOfLiveRunning()) {
                    stopGameOfLive();
                    startRestartButton.setIcon(IconProvider.getIcon(IconProvider.Icon.START));
                    startRestartButton.setToolTipText("Start the game of life");
                } else {
                    startGameOfLive();
                    startRestartButton.setIcon(IconProvider.getIcon(IconProvider.Icon.STOP));
                    startRestartButton.setToolTipText("Stop the game of life");
                }
        });

        // next button
        topControlPanel.addNextGenerationBtnActionListener((e) -> triggerNextGeneration());

        // reset/clear button
        topControlPanel.addResetClearBtnActionListener((e) -> {
            // if the current generation is not the first generation...
            if (gameOfLife.getGenerationCounter() > 1)
                //... reset to the first generation
                gameOfLife.resetToFirstGeneration();
            else
                // ...otherwise reset the game of life (clear the field)
                gameOfLife.resetGameOfLife();
        });

        // delay slider
        topControlPanel.addDelaySliderChangeListener((e) -> {
            if (e.getSource() instanceof JSlider delaySlider) {
                int currentDelay = delay;
                int currentValue = delaySlider.getValue();
                // only change speed if slider has final result
                // or if the difference between current slider value and current speed is greater than 3
                if (delaySlider.getValueIsAdjusting() || Math.abs(currentValue - currentDelay) > 3) {
                    // delayFactor for more subtle delay adjustment
                    int delayFactor = 100;
                    if (currentValue > (delaySlider.getMaximum() - delaySlider.getMinimum()) / 2)
                        delayFactor = 75;
                    setDelay((delaySlider.getMaximum() + 1 - currentValue) * delayFactor);
                }
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
