package main.model;

import main.view.Observer;

import java.util.ArrayList;
import java.util.List;

public class GameOfLife implements Observable {
    private final GameOfLifeField gameOfLifeField;

    private int generationCounter = 1;

    private final List<Observer> observers = new ArrayList<>();

    /**
     * List which contains the positions of cells that have a new life state.
     * This list is passed to the observers for them to update their view.
     * After that this list will be emptied.
     */
    final ArrayList<int[]> cellsToBeUpdated = new ArrayList<>();

    /**
     * Create a gameOfLife.
     *
     * @param fieldHeight The height of the gameOfLife field
     * @param fieldWidth  The width of the gameOfLife field
     */
    public GameOfLife(int fieldHeight, int fieldWidth) {
        gameOfLifeField = new GameOfLifeField(fieldHeight, fieldWidth);
    }

    /**
     * Set one cell alive or not alive at the given row and column in the field.
     * The registered observers get notified.
     *
     * @param row    row of the cell
     * @param column column of the cell
     * @param alive  whether the cell should be alive or dead
     */
    public void setCellAt(int row, int column, boolean alive) {
        gameOfLifeField.setCellAt(row, column, alive);

        cellsToBeUpdated.add(new int[]{row, column});
        notifyObservers();
    }

    /**
     * Loads the next generation of the game of life.
     * The registered observers get notified.
     */
    public void loadNextGeneration() {
        cellsToBeUpdated.addAll(gameOfLifeField.getNextGeneration());
        notifyObservers();
    }

    /**
     * Kills all cells in the gameOfLife field.
     * The registered observers get notified.
     */
    public void killAllCells() {
        cellsToBeUpdated.addAll(gameOfLifeField.killAllCells());
        notifyObservers();
    }

    /**
     * Kills all cells in the field except at the given coordinates (row, column).
     * The registered observers get notified.
     *
     * @param cellPositions coordinates (row, column) of cells that should not be killed
     */
    public void killAllCellsExceptOf(int[]... cellPositions) {
        cellsToBeUpdated.addAll(gameOfLifeField.killAllCellsExceptOf(cellPositions));
        notifyObservers();
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

    public int getGenerationCounter() {
        return generationCounter;
    }

    public int getFieldHeight() {
        return gameOfLifeField.getHeight();
    }

    public int getFieldWidth() {
        return gameOfLifeField.getWidth();
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
