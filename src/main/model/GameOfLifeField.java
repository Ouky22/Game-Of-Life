package main.model;

import main.view.Observer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains the logic and data for the game of life
 */
public class GameOfLifeField implements Observable {
    private final boolean[][] field;
    private final int WIDTH;
    private final int HEIGHT;

    private int generationCounter = 1;

    private final List<Observer> observers = new ArrayList<>();

    /**
     * List which contains the positions of cells that have a new life state.
     * This list is passed to the observers for them to update their view.
     * After that this list will be emptied.
     */
    final ArrayList<int[]> cellsToBeUpdated = new ArrayList<>();

    public GameOfLifeField(int height, int width) {
        field = new boolean[height][width];
        WIDTH = width;
        HEIGHT = height;
    }


    /**
     * Set one cell alive or not alive at the given row and column in the field.
     * It will notify the observers and pass them the position of the cell whose life state changed.
     *
     * @param row    row of the cell
     * @param column column of the cell
     * @param alive  whether the cell should be alive or dead
     */
    public void setCellAt(int row, int column, boolean alive) {
        if (!isCoordinateInField(row, column))
            return;

        field[row][column] = alive;

        cellsToBeUpdated.add(new int[]{row, column});
        notifyObservers();
    }

    /**
     * Loads the next generation of the game of life.
     * It will notify the observers and pass them the positions of cells whose life state changed.
     */
    public void loadNextGeneration() {
        // add coordinates of cells with a new life state
        ArrayList<int[]> toggledCells = new ArrayList<>();

        // apply rules of game of live
        for (int row = 0; row < HEIGHT; row++)
            for (int col = 0; col < WIDTH; col++) {
                int neighboursAmount = getAmountLivingNeighbours(row, col);
                boolean alive = field[row][col];

                if (!alive && neighboursAmount == 3) // bring dead cell to live
                    toggledCells.add(new int[]{row, col});
                else if (alive && (neighboursAmount < 2 || neighboursAmount > 3)) // cell dies
                    toggledCells.add(new int[]{row, col});
            }

        // change state of cells in field
        for (int[] coordinate : toggledCells)
            field[coordinate[0]][coordinate[1]] = !field[coordinate[0]][coordinate[1]];

        cellsToBeUpdated.addAll(toggledCells);
        notifyObservers();
    }

    /**
     * Kills all cells in the field.
     * It will notify the observers and pass them the positions of cells who got killed.
     */
    public void killAllCells() {
        ArrayList<int[]> killedCells = new ArrayList<>();

        for (int row = 0; row < field.length; row++)
            for (int col = 0; col < field[row].length; col++) {
                // if cell is alive...
                if (field[row][col]) {
                    // ...kill it
                    field[row][col] = false;
                    killedCells.add(new int[]{row, col});
                }
            }
        cellsToBeUpdated.addAll(killedCells);
        notifyObservers();
    }

    /**
     * Kills al cells in the field except at the given coordinates
     * It will notify the observers and pass them the positions of cells who got killed.
     *
     * @param cellPositions coordinates of cells that should not be killed
     */
    public void killAllCellsExceptOf(int[]... cellPositions) {
        ArrayList<int[]> killedCells = new ArrayList<>();

        for (int row = 0; row < field.length; row++)
            for (int col = 0; col < field[row].length; col++) {
                boolean shouldBeKilled = true;

                // check if the current position matches one of the positions of the cells which
                // should not be killed
                for (int[] sparedCellPos : cellPositions)
                    if (Arrays.equals(sparedCellPos, new int[]{row, col})) {
                        shouldBeKilled = false;
                        break;
                    }

                // if cell should be killed and is alive
                if (shouldBeKilled && field[row][col]) {
                    // ...kill it
                    field[row][col] = false;
                    killedCells.add(new int[]{row, col});
                }
            }

        cellsToBeUpdated.addAll(killedCells);
        notifyObservers();
    }

    /**
     * Increments the generation counter.
     * Will notify the registered observers.
     */
    public void incrementGenerationCounter() {
        generationCounter++;
        notifyObservers();
    }

    /**
     * Reset the generation counter to 1.
     * Will notify the registered observers.
     */
    public void resetGenerationCounter() {
        generationCounter = 1;
        notifyObservers();
    }


    /**
     * Check if cell at given coordinate is alive
     *
     * @param coordinate of the cell
     * @return whether the cell is alive or dead
     */
    public boolean isCellAliveAt(int[] coordinate) {
        int row = coordinate[0];
        int column = coordinate[1];
        return field[row][column];
    }

    /**
     * Clear the list of cells which has a new life state since the last time this method was called
     * or, if it is the first call, since the game was started.
     *
     * @return The positions of cells which got a new life state.
     */
    public ArrayList<int[]> clearCellsToBeUpdated() {
        final ArrayList<int[]> copy = new ArrayList<>(cellsToBeUpdated);
        cellsToBeUpdated.clear();
        return copy;
    }

    public int getGenerationCounter() {
        return generationCounter;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public int getWidth() {
        return WIDTH;
    }

    boolean isCoordinateInField(int row, int column) {
        return row >= 0 && row < HEIGHT && column >= 0 && column < WIDTH;
    }


    /**
     * Return row, which is next to the given row, if given row is outside the field boundaries.
     * If the given row is inside the field boundaries, the given row will be returned.
     *
     * @param row given row, which could be outside the field boundaries
     * @return the next row
     */
    int getNextTorusRow(int row) {
        if (row < 0)
            return HEIGHT - 1;
        if (row >= HEIGHT)
            return 0;
        return row;
    }

    /**
     * Return column, which is next to the given column, if given column is outside the field boundaries.
     * If the given column is inside the field boundaries, the given column will be returned.
     *
     * @param column given column, which could be outside the field boundaries
     * @return the next column
     */
    int getNextTorusColumn(int column) {
        if (column < 0)
            return WIDTH - 1;
        if (column >= WIDTH)
            return 0;
        return column;
    }

    int getAmountLivingNeighbours(int row, int column) {
        if (!isCoordinateInField(row, column))
            return 0;

        int counter = 0;
        for (int i = row - 1; i <= row + 1; i++)
            for (int k = column - 1; k <= column + 1; k++) {
                // adapt coordinates if they are outside the field boundaries
                int torusRow = getNextTorusRow(i);
                int torusColumn = getNextTorusColumn(k);

                if ((torusRow != row || torusColumn != column) && field[torusRow][torusColumn])
                    counter++;
            }
        return counter;
    }

    boolean[][] getField() {
        return field;
    }


    @Override
    public void register(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void unregister(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers)
            o.update();
    }
}





















