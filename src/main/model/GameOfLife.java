package main.model;

import main.view.Observer;

import java.util.ArrayList;
import java.util.Arrays;

public class GameOfLife implements Observable {
    private final GameOfLifeField gameOfLifeField;

    private int generationCounter = 1;

    private final ArrayList<Observer> observers = new ArrayList<>();

    /**
     * List which contains the positions of cells that have a new life state.
     * This list is passed to the observers for them to update their view.
     * After that this list will be emptied.
     */
    private final ArrayList<int[]> cellsToBeUpdated = new ArrayList<>();

    // contains the positions of the living cells of the first generation
    private final ArrayList<int[]> firstGenCellPositions = new ArrayList<>();


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
     * If it is the first generation, the cell position is stored in a list containing
     * the positions of the cells of the first generation.
     * The registered observers get notified.
     *
     * @param row    row of the cell
     * @param column column of the cell
     * @param alive  whether the cell should be alive or dead
     */
    public void setCellAt(int row, int column, boolean alive) {
        // if the coordinate is outside the field, return
        if (!gameOfLifeField.setCellAt(row, column, alive))
            return;

        // if it is the first generation, the positions of the living cells
        // in the firstGenCellPositions list have to be updated
        if (generationCounter == 1)
            updateFirstGenCellPositions(new int[]{row, column}, alive);

        cellsToBeUpdated.add(new int[]{row, column});
        notifyObservers();
    }

    /**
     * Kills all cells except the cells from the first generation.
     * If cells from the first generation are dead, they are brought back to life.
     * Notifies the registered observers.
     */
    public void resetToFirstGeneration() {
        // kill all cells that are not first generation cells.
        // Save them in a list containing all cells which have a new life state.
        ArrayList<int[]> toggledCells = gameOfLifeField.killAllCellsExceptOf(firstGenCellPositions);

        // if a cell from the first generation is not alive, bring it back to life
        for (int[] coordinate : firstGenCellPositions)
            if (!gameOfLifeField.isCellAliveAt(coordinate)) {
                gameOfLifeField.setCellAt(coordinate[0], coordinate[1], true);
                toggledCells.add(coordinate);
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
        firstGenCellPositions.clear();
        resetGenerationCounter();
        cellsToBeUpdated.addAll(gameOfLifeField.killAllCells());
        notifyObservers();
    }

    /**
     * Updates the firstGenCellPositions list.
     * It makes sure that cell positions in the firstGenCellPositions list only occur at most once.
     *
     * @param coordinate Coordinate of the cell
     * @param addIt      Whether the cell position should be added to or removed from the firstGenCellPositions list
     */
    private void updateFirstGenCellPositions(int[] coordinate, boolean addIt) {
        // index of the given coordinate in the firstGenCellPositions list.
        int coordinateIndex = -1;
        // get the index of the coordinate in the firstGenCellPositions list. If it is not in the list, the index is -1.
        for (int i = 0; i < firstGenCellPositions.size(); i++)
            if (Arrays.equals(firstGenCellPositions.get(i), coordinate))
                coordinateIndex = i;

        // if the cell should be added and is not already in the firstGenCellPositions list, add it to firstGenCellPositions
        if (addIt && coordinateIndex == -1)
            firstGenCellPositions.add(coordinate);
            // if the cell should be removed and is in the firstGenCellPositions list, remove it from firstGenCellPositions
        else if (!addIt && coordinateIndex >= 0)
            firstGenCellPositions.remove(coordinateIndex);
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
    public ArrayList<int[]> clearCellsToBeUpdated() {
        final ArrayList<int[]> copy = new ArrayList<>(cellsToBeUpdated);
        cellsToBeUpdated.clear();
        return copy;
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

    public double getLivingCellsCoverage() {
        return gameOfLifeField.getLivingCellsCoverage();
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
