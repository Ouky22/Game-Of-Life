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
     * List which contains the positions of cells that have a new life state.
     * This list is passed to the observers for them to update their view.
     * After that this list will be emptied.
     */
    private final ArrayList<int[]> cellsToBeUpdated = new ArrayList<>();

    /* contains the coordinates and colors of the living cells of the first generation.
     The coordinate is stored as a string in the following form: "row.column"
     This makes sure each coordinate does only occur at most once in this HashMap.
     To use the string coordinate, it has to be converted into numeric values (such methods are provided in this class)
    */
    private final HashMap<String, Color> firstGeneration = new HashMap<>();


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
     * Kills a cell at the given coordinate and its color is set to the color of dead cells.
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
        // Save them in a list containing all cells which have a new life state.
        ArrayList<int[]> firstGenCellsPositions = new ArrayList<>();
        for (String stringCoordinate : firstGeneration.keySet())
            firstGenCellsPositions.add(getCoordinateAsArray(stringCoordinate));
        ArrayList<int[]> toggledCells = gameOfLifeField.killAllCellsExceptOf(firstGenCellsPositions);

        // if a cell from the first generation is not alive, bring it back to life
        for (String stringCoordinate : firstGeneration.keySet()) {
            int[] coordinate = getCoordinateAsArray(stringCoordinate);
            if (!gameOfLifeField.isCellAliveAt(coordinate)) {
                gameOfLifeField.setCellAt(coordinate[0], coordinate[1], true, firstGeneration.get(stringCoordinate));
                toggledCells.add(coordinate);
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
     * Go to a certain generation with a valid generation number.
     *
     * @param generation The number of the generation
     */
    public void goToGeneration(int generation) {
        // 0 and negative numbers are not valid generation numbers.
        // Going to the current generation does not need any changes.
        if (generation <= 0 || generation == generationCounter)
            return;

        // TODO implement logic
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
                firstGeneration.put(getCoordinateAsString(new int[]{row, column}), cellColor);
            else // otherwise, the cell got killed and should be removed from the first generation
                firstGeneration.remove(getCoordinateAsString(new int[]{row, column}));
        }

        cellsToBeUpdated.add(new int[]{row, column});
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

    /**
     * @param stringCoordinate string coordinate in following form: "row.column".
     * @return the array coordinate extracted from the string coordinate
     */
    private int[] getCoordinateAsArray(String stringCoordinate) {
        int[] coordinate = new int[2];
        for (int i = 0; i < 2; i++)
            coordinate[i] = Integer.parseInt(stringCoordinate.split("\\.")[i]);
        return coordinate;
    }

    /**
     * @param arrayCoordinate coordinate as int array with length of 2 containing the row and column
     * @return the string coordinate extracted from the array coordinate
     */
    private String getCoordinateAsString(int[] arrayCoordinate) {
        StringBuilder stringCoordinate = new StringBuilder();
        for (int i = 0; i < 2; i++)
            stringCoordinate.append(arrayCoordinate[i]).append(".");
        return stringCoordinate.toString();
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
