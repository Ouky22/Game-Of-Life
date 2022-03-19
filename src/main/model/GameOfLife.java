package main.model;

import main.view.Observer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    /**
     * This HashMap contains all manipulations made by the user (revive/kill cell and set cell color) in each generation
     * (if there is no manipulation in a generation, then its generation number is not a key of this HashMap).
     * If there is manipulation, this HashMap saves copies of the manipulated cells,
     * which represent their state in the generation in which they got manipulated.
     * key -> generation number
     * value -> copies of cells which were manipulated by the user
     */
    private final HashMap<Integer, ArrayList<GofCell>> manipulatedCells = new HashMap<>();

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
        // if the coordinate is outside the field, return
        if (!gameOfLifeField.setCellAt(row, column, true, cellColor))
            return;

        updateManipulatedCells(row, column);

        cellsToBeUpdated.add(gameOfLifeField.getCellAt(row, column));
        notifyObservers();
    }

    /**
     * Kills a cell at the given coordinate and its color is set to the default color of dead cells.
     */
    public void killCellAt(int row, int column) {
        // if the coordinate is outside the field, return
        if (!gameOfLifeField.setCellAt(row, column, false, GofCell.DEAD_CELL_COLOR))
            return;

        updateManipulatedCells(row, column);

        cellsToBeUpdated.add(gameOfLifeField.getCellAt(row, column));
        notifyObservers();
    }

    /**
     * Kills all cells except the cells from the first generation.
     * If cells from the first generation are dead, they are brought back to life.
     * Notifies the registered observers.
     */
    public void resetToFirstGeneration() {
        // kill all cells
        ArrayList<GofCell> toggledCells = gameOfLifeField.killAllCells();

        // get the manipulated cells of the first generation (the cells which were brought to life)
        // and bring them to life
        for (GofCell firstGenCell : manipulatedCells.get(1)) {
            gameOfLifeField.setCellAt(firstGenCell.getRow(), firstGenCell.getColumn(), firstGenCell.isAlive(), firstGenCell.getColor());
            toggledCells.add(firstGenCell);
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
        manipulatedCells.clear();
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

        // if there are manipulations saved in manipulatedCells for this generation, apply them to this generation
        if (manipulatedCells.get(generationCounter) != null)
            for (GofCell cell : manipulatedCells.get(generationCounter)) {
                gameOfLifeField.setCellAt(cell.getRow(), cell.getColumn(), cell.isAlive(), cell.getColor());
                cellsToBeUpdated.add(cell);
            }
        notifyObservers();
    }

    /**
     * Go to a certain generation with a valid generation number.
     *
     * @param generation The number of the generation
     */
    public void goToGeneration(int generation) {
        // 0 and negative numbers are not valid generation numbers.
        // Going to the current generation does not need any changes.
        if (generation <= 0 || generation == generationCounter)
            return;


        int startIndex = 1;
        // start at the current generation if the requested generation comes after the current one
        if (generation > generationCounter)
            startIndex = generationCounter;
        else // if the generation comes before the current generation, reset the game of life
            resetToFirstGeneration();

        // load all following generations starting with the startIndex
        for (int i = startIndex; i < generation; i++) {
            cellsToBeUpdated.addAll(gameOfLifeField.getNextGeneration());
            generationCounter++;
            // if there are manipulations saved in manipulatedCells for this generation, apply them to this generation
            if (manipulatedCells.get(generationCounter) != null)
                for (GofCell cell : manipulatedCells.get(generationCounter)) {
                    gameOfLifeField.setCellAt(cell.getRow(), cell.getColumn(), cell.isAlive(), cell.getColor());
                    cellsToBeUpdated.add(cell);
                }
        }

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
     * Updates the container that contains all cells manipulated by the user in each generation.
     *
     * @param row    row of the cell which got a new life state
     * @param column column of the cell which got a new life state
     */
    private void updateManipulatedCells(int row, int column) {
        // create a copy of the cell which got manipulated
        GofCell manipulatedCellCopy = gameOfLifeField.getCellAt(row, column).clone();

        // get the list of manipulations related to the current generation.
        // If there is no list related to the current generation number or the current generation number is not present
        // as a key inside manipulatedCells, generate a list for the current generation number
        // and use it to initialize a local variable.
        ArrayList<GofCell> manipulations = manipulatedCells.computeIfAbsent(generationCounter, k -> new ArrayList<>());
        manipulations.add(manipulatedCellCopy);
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




















