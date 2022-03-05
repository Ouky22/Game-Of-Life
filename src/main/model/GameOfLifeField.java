package main.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Contains the logic and data for the game of life
 */
public class GameOfLifeField {
    private final boolean[][] field;
    private final int WIDTH;
    private final int HEIGHT;

    /**
     * Counter for how many cells are alive
     */
    private int livingCellsCounter = 0;


    /**
     * Create a field for the gameOfLife.
     *
     * @param height The height of the field
     * @param width  The width of the field
     */
    GameOfLifeField(int height, int width) {
        field = new boolean[height][width];
        WIDTH = width;
        HEIGHT = height;
    }

    /**
     * Set one cell alive or not alive at the given row and column in the field.
     *
     * @param row    row of the cell
     * @param column column of the cell
     * @param alive  whether the cell should be alive or dead
     * @return returns false if row or column are outside the field and true if operation was successful
     */
    boolean setCellAt(int row, int column, boolean alive) {
        if (!isCoordinateInField(row, column))
            return false;

        field[row][column] = alive;
        livingCellsCounter++;
        return true;
    }

    /**
     * Loads the next generation of the game of life.
     *
     * @return Returns the cells which got a new life state
     */
    ArrayList<int[]> getNextGeneration() {
        // add coordinates of cells with a new life state
        ArrayList<int[]> toggledCells = new ArrayList<>();

        // apply rules of game of life
        for (int row = 0; row < HEIGHT; row++)
            for (int col = 0; col < WIDTH; col++) {
                int neighboursAmount = getAmountLivingNeighbours(row, col);
                boolean alive = field[row][col];

                if (!alive && neighboursAmount == 3) { // bring dead cell to life
                    toggledCells.add(new int[]{row, col});
                    livingCellsCounter++;
                } else if (alive && (neighboursAmount < 2 || neighboursAmount > 3)) { // cell dies
                    toggledCells.add(new int[]{row, col});
                    livingCellsCounter--;
                }
            }

        // change state of cells in field
        for (int[] coordinate : toggledCells)
            field[coordinate[0]][coordinate[1]] = !field[coordinate[0]][coordinate[1]];

        return toggledCells;
    }

    /**
     * Kills all cells in the field.
     */
    ArrayList<int[]> killAllCells() {
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
        livingCellsCounter = 0;
        return killedCells;
    }

    /**
     * Kills all cells in the field except at the given coordinates (row, column).
     *
     * @param cellPositions coordinates (row, column) of cells that should not be killed
     */
    ArrayList<int[]> killAllCellsExceptOf(ArrayList<int[]> cellPositions) {
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
                    livingCellsCounter--;
                }
            }

        return killedCells;
    }

    /**
     * Check if cell at given coordinate is alive
     *
     * @param coordinate of the cell
     * @return whether the cell is alive or dead
     */
    boolean isCellAliveAt(int[] coordinate) {
        int row = coordinate[0];
        int column = coordinate[1];
        return field[row][column];
    }

    /**
     * @return What percentage of the field is living cells. The value is rounded to one decimal place.
     */
    public double getLivingCellsCoverage() {
        double preciseCoverage = (double) livingCellsCounter / (WIDTH * HEIGHT);
        int temp = (int) (preciseCoverage * 1000.0);
        return ((double) temp) / 10.0;
    }

    int getHeight() {
        return HEIGHT;
    }

    int getWidth() {
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
}





