package main.model;

import main.view.Observer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GameOfLife implements Observable {
    private final GameOfLifeField gameOfLifeField;

    private int generationCounter = 1;

    private final ArrayList<Observer> observers = new ArrayList<>();

    /*
     * List which contains the cells that have a new life state.
     * This list is passed to the observers for them to update their view.
     * After that this list will be emptied.
     */
    private final ArrayList<GofCell> cellsToBeUpdated = new ArrayList<>();

    /*
    Contains the cells that were alive in the first generation and the color they had in the first generation
    */
    private final HashMap<GofCell, Color> firstGeneration = new HashMap<>();


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
     * Brings a cell at the given coordinate to life and its color is set to the given one.
     */
    public void reviveCellAt(int row, int column, Color cellColor) {
        setCellAt(row, column, true, cellColor);
    }

    /**
     * Kills a cell at the given coordinate and its color is set to the default color of dead cells.
     */
    public void killCellAt(int row, int column) {
        setCellAt(row, column, false, GofCell.DEAD_CELL_COLOR);
    }

    /**
     * Kills all cells except the cells from the first generation.
     * If cells from the first generation are dead, they are brought back to life.
     * Notifies the registered observers.
     */
    public void resetToFirstGeneration() {
        // kill all cells that are not first generation cells.
        ArrayList<GofCell> toggledCells = gameOfLifeField.killAllCellsExceptOf(new ArrayList<>(firstGeneration.keySet()));

        // if a cell from the first generation is not alive, bring it back to life
        for (GofCell firstGenCell : firstGeneration.keySet()) {
            if (!firstGenCell.isAlive()) {
                gameOfLifeField.setCellAt(firstGenCell.getRow(), firstGenCell.getColumn(), true, firstGeneration.get(firstGenCell));
                toggledCells.add(firstGenCell);
            }
        }

        resetGenerationCounter();
        cellsToBeUpdated.addAll(toggledCells);
        notifyObservers();
    }

    /**
     * Kills all cells in the field and the game of life starts with a new (empty) first generation.
     * Notifies the registered observers.
     */
    public void resetGameOfLife() {
        firstGeneration.clear();
        resetGenerationCounter();
        cellsToBeUpdated.addAll(gameOfLifeField.killAllCells());
        notifyObservers();
    }

    /**
     * Loads the next generation of the game of life.
     * The registered observers get notified.
     */
    public void loadNextGeneration() {
        cellsToBeUpdated.addAll(gameOfLifeField.getNextGeneration());
        generationCounter++;
        notifyObservers();
    }

    /**
     * Clear the list of cells which has a new life state since the last time this method was called
     * or, if it is the first call, since the game was started.
     *
     * @return The positions of cells which got a new life state.
     */
    public ArrayList<GofCell> clearCellsToBeUpdated() {
        final ArrayList<GofCell> copy = new ArrayList<>(cellsToBeUpdated);
        cellsToBeUpdated.clear();
        return copy;
    }

    public int getGenerationCounter() {
        return generationCounter;
    }

    /**
     * @return the color at the given coordinate. If the coordinate is outside the field, null is returned
     */
    public Color getCellColorAt(int row, int column) {
        return gameOfLifeField.getCellColorAt(row, column);
    }

    public int getFieldHeight() {
        return gameOfLifeField.getHeight();
    }

    public int getFieldWidth() {
        return gameOfLifeField.getWidth();
    }

    public double getLivingCellsCoverage() {
        return gameOfLifeField.getLivingCellsCoverage();
    }

    /**
     * Set the life state and the color of one cell in the field at the given coordinate (row, column).
     * If it is the first generation, the container containing the cells of the first generation will be updated.
     * The registered observers get notified.
     *
     * @param row       row of the cell
     * @param column    column of the cell
     * @param cellColor color of cell
     * @param alive     whether the cell should be alive or dead
     */
    private void setCellAt(int row, int column, boolean alive, Color cellColor) {
        // if the coordinate is outside the field, return
        if (!gameOfLifeField.setCellAt(row, column, alive, cellColor))
            return;

        // if it is the first generation, is has to be updated
        if (generationCounter == 1) {
            // if cell was brought to life, add it to the first generation
            if (alive)
                firstGeneration.put(gameOfLifeField.getCellAt(row, column), gameOfLifeField.getCellColorAt(row, column));
            else // otherwise, the cell got killed and should be removed from the first generation
                firstGeneration.remove(gameOfLifeField.getCellAt(row, column));
        }

        cellsToBeUpdated.add(gameOfLifeField.getCellAt(row, column));
        notifyObservers();
    }

    /**
     * Reset the generation counter to 1.
     * Will notify the registered observers.
     */
    private void resetGenerationCounter() {
        generationCounter = 1;
        notifyObservers();
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
