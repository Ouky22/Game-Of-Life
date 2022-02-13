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
        timer.setInitialDelay(100);
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
        timer.restart();
    }

    public void stopGameOfLive() {
        timer.stop();
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
