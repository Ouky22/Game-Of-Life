package domain;


import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameOfLiveManager {
    private final GameOfLiveField gameOfLiveField;
    private int delay = 1000;
    private final Timer timer;

    public GameOfLiveManager(int fieldHeight, int fieldWidth, ActionListener timerListener) {
        this.gameOfLiveField = new GameOfLiveField(fieldHeight, fieldWidth);
        this.timer = new Timer(delay, timerListener);
        timer.setInitialDelay(50);
    }

    public ArrayList<int[]> loadNextGeneration() {
        return gameOfLiveField.loadNextGeneration();
    }

    /**
     * set one cell in the gameOfLive field at the given row and column alive or not alive
     *
     * @param row    row of the cell
     * @param column column of the cell
     * @param alive  whether the cell should be alive or dead
     */
    public void setCellAt(int row, int column, boolean alive) {
        gameOfLiveField.setCellAt(row, column, alive);
    }

    public void startGameOfLive() {
        timer.setRepeats(true);
        timer.restart();
    }

    public void stopGameOfLive() {
        timer.stop();
    }

    /**
     * trigger timer so the next generation will be generated and drawn.
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

    public int getDelay() {
        return delay;
    }

    /**
     * sets the delay between each Generation
     * @param delay in milliseconds
     */
    public void setDelay(int delay) {
        this.delay = delay;
        this.timer.setDelay(delay);
    }

    public int getFieldHeight() {
        return gameOfLiveField.getHeight();
    }

    public int getFieldWidth() {
        return gameOfLiveField.getWidth();
    }
}
