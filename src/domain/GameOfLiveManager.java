package domain;


import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class GameOfLiveManager {
    private final GameOfLiveField gameOfLiveField;
    private int delay = 1000;
    private final Timer timer;

    private int generationCounter = 1;

    // contains the positions of the cells in the first generation
    private final ArrayList<int[]> firstGenCellPositions = new ArrayList<>();

    public GameOfLiveManager(int fieldHeight, int fieldWidth, ActionListener timerListener) {
        this.gameOfLiveField = new GameOfLiveField(fieldHeight, fieldWidth);
        this.timer = new Timer(delay, timerListener);
        timer.setInitialDelay(50);
    }

    /**
     * loads the next generation of the game of life
     *
     * @return coordinates of cells which got a new life status
     */
    public ArrayList<int[]> loadNextGeneration() {
        generationCounter++;
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

        // if it is the first generation, the positions of the living cells
        // needs to be updated in the firstGenCellPositions list. Otherwise, return.
        if (generationCounter != 1)
            return;

        // if a cell is brought to life, add it to firstGenCellPositions
        if (alive) {
            firstGenCellPositions.add(new int[]{row, column});
            return;
        }

        // if a cell is killed, remove it from firstGenCellPositions
        for (int i = 0; i < firstGenCellPositions.size(); i++)
            if (firstGenCellPositions.get(i)[0] == row && firstGenCellPositions.get(i)[1] == column) {
                firstGenCellPositions.remove(i);
                break;
            }
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

    /**
     * Kills all cells in the field except the cells from the first generation.
     * If cells from the first generation are dead, they are brought back to live.
     *
     * @return coordinates of cells which got a new life status (deed or alive)
     */
    public ArrayList<int[]> resetToFirstGeneration() {
        // convert the startCellPositions list to array
        int[][] startPositionsArray = new int[firstGenCellPositions.size()][2];
        for (int row = 0; row < firstGenCellPositions.size(); row++)
            startPositionsArray[row] = firstGenCellPositions.get(row);

        // kill all cells in the field except the cell(s) from the first generation (in startCellPositions)
        ArrayList<int[]> toggledCells = gameOfLiveField.killAllCellsExceptOf(startPositionsArray);

        // if a cell from the first generation is dead, bring it back to life
        for (int[] coordinate : firstGenCellPositions)
            if (!gameOfLiveField.isCellAliveAt(coordinate)) {
                setCellAt(coordinate[0], coordinate[1], true);
                toggledCells.add(coordinate);
            }
        generationCounter = 1;

        return toggledCells;
    }

    /**
     * Kills all cells in the field
     *
     * @return coordinates of cells which got killed
     */
    public ArrayList<int[]> killAllCells() {
        firstGenCellPositions.clear();
        return gameOfLiveField.killAllCells();
    }

    public boolean isGameOfLiveRunning() {
        return timer.isRunning();
    }

    public int getDelay() {
        return delay;
    }

    /**
     * sets the delay between each Generation
     *
     * @param delay in milliseconds
     */
    public void setDelay(int delay) {
        this.delay = delay;
        this.timer.setDelay(delay);
    }

    public int getGenerationCounter() {
        return generationCounter;
    }

    public int getFieldHeight() {
        return gameOfLiveField.getHeight();
    }

    public int getFieldWidth() {
        return gameOfLiveField.getWidth();
    }
}
