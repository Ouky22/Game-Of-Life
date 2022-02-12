package domain;


import java.util.ArrayList;

public class GameOfLiveManager {
    private GameOfLiveField gameOfLiveField;
    private int speed = 1000;

    public GameOfLiveManager(int fieldHeight, int fieldWidth) {
        gameOfLiveField = new GameOfLiveField(fieldHeight, fieldWidth);
        this.speed = speed;
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getFieldHeight() {
        return gameOfLiveField.getHeight();
    }

    public int getFieldWidth() {
        return gameOfLiveField.getWidth();
    }
}
