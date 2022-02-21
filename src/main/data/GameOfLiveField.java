package main.data;

import java.util.ArrayList;
import java.util.Arrays;

public class GameOfLiveField {
    private final boolean[][] field;
    private final int WIDTH;
    private final int HEIGHT;

    public GameOfLiveField(int height, int width) {
        field = new boolean[height][width];
        WIDTH = width;
        HEIGHT = height;
    }

    /**
     * set one cell in the field at the given row and column alive or not alive
     *
     * @param row    row of the cell
     * @param column column of the cell
     * @param alive  whether the cell should be alive or dead
     */
    public void setCellAt(int row, int column, boolean alive) {
        if (!isCoordinateInField(row, column))
            return;

        field[row][column] = alive;
    }

    boolean[][] getField() {
        return field;
    }

    /**
     * loads the next generation of cells.
     *
     * @return all cells that got a new life status (so the life status got toggled)
     */
    public ArrayList<int[]> loadNextGeneration() {
        // add coordinates of cells with a new life status
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

        // change status of cells in field
        for (int[] coordinate : toggledCells)
            field[coordinate[0]][coordinate[1]] = !field[coordinate[0]][coordinate[1]];

        return toggledCells;
    }

    /**
     * Kills all cells in the field
     *
     * @return coordinates of cells which got killed
     */
    public ArrayList<int[]> killAllCells() {
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
        return killedCells;
    }

    /**
     * Kills al cells in the field except at the given coordinates
     *
     * @param cellPositions coordinates of cells that should not be killed
     * @return coordinates of cells which got killed
     */
    public ArrayList<int[]> killAllCellsExceptOf(int[]... cellPositions) {
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
        return killedCells;
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

    boolean isCoordinateInField(int row, int column) {
        return row >= 0 && row < HEIGHT && column >= 0 && column < WIDTH;
    }

    /**
     * return row, which is next to the given row, if given row is outside the field boundaries.
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
     * return column, which is next to the given column, if given column is outside the field boundaries.
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

    /**
     * check if cell at given coordinate is alive
     *
     * @param coordinate of the cell
     * @return whether the cell is alive or dead
     */
    public boolean isCellAliveAt(int[] coordinate) {
        int row = coordinate[0];
        int column = coordinate[1];
        return field[row][column];
    }

    public int getHeight() {
        return HEIGHT;
    }

    public int getWidth() {
        return WIDTH;
    }
}





















