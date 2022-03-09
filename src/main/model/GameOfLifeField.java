package main.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Contains the logic and data for the game of life
 */
public class GameOfLifeField {
    private final GofCell[][] field;
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
        // create field and fill it with GofCells
        field = new GofCell[height][width];
        for (int row = 0; row < field.length; row++)
            for (int col = 0; col < field[row].length; col++)
                field[row][col] = new GofCell();

        WIDTH = width;
        HEIGHT = height;
    }

    /**
     * Set the life state and the color of one cell in the field at the given coordinate (row, column),
     * if the coordinate is inside the field boundaries.
     *
     * @param row       row of the cell
     * @param column    column of the cell
     * @param alive     whether the cell should be alive or dead
     * @param cellColor color of cell
     * @return returns false if row or column are outside the field and true if operation was successful
     */
    boolean setCellAt(int row, int column, boolean alive, Color cellColor) {
        if (!isCoordinateInField(row, column))
            return false;

        field[row][column].setAlive(alive);
        field[row][column].setColor(cellColor);
        livingCellsCounter++;
        return true;
    }

    /**
     * Loads the next generation of the game of life.
     *
     * @return Returns the cells which got a new life state
     */
    ArrayList<int[]> getNextGeneration() {
        // contains the positions and colors of cells whose life state needs to be toggled
        HashMap<int[], Color> cellPositions = new HashMap<>();

        // apply rules of game of life
        for (int row = 0; row < HEIGHT; row++)
            for (int col = 0; col < WIDTH; col++) {
                int neighboursAmount = getAmountLivingNeighbours(row, col);
                boolean alive = field[row][col].isAlive();

                if (!alive && neighboursAmount == 3) { // dead cell becomes alive
                    cellPositions.put(new int[]{row, col}, getMostFrequentlyColor(row, col));
                    livingCellsCounter++;
                } else if (alive && (neighboursAmount < 2 || neighboursAmount > 3)) { // cell dies
                    cellPositions.put(new int[]{row, col}, GofCell.DEAD_CELL_COLOR);
                    livingCellsCounter--;
                }
            }

        // toggle the life state at every cell position in the field
        for (int[] coordinate : cellPositions.keySet()) {
            GofCell currentCell = field[coordinate[0]][coordinate[1]];
            // if cell is alive, kill it
            if (currentCell.isAlive()) {
                currentCell.setAlive(false);
                currentCell.setColor(GofCell.DEAD_CELL_COLOR);
            } else { // if cell is dead, bring it to life
                currentCell.setAlive(true);
                currentCell.setColor(cellPositions.get(coordinate));
            }
        }

        return new ArrayList<>(cellPositions.keySet());
    }

    /**
     * Kills all cells in the field.
     */
    ArrayList<int[]> killAllCells() {
        ArrayList<int[]> killedCells = new ArrayList<>();

        for (int row = 0; row < field.length; row++)
            for (int col = 0; col < field[row].length; col++) {
                // if cell is alive...
                if (field[row][col].isAlive()) {
                    // ...kill it
                    field[row][col].setAlive(false);
                    field[row][col].setColor(GofCell.DEAD_CELL_COLOR);
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
                if (shouldBeKilled && field[row][col].isAlive()) {
                    // ...kill it
                    field[row][col].setAlive(false);
                    field[row][col].setColor(GofCell.DEAD_CELL_COLOR);
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
        return field[row][column].isAlive();
    }

    /**
     * @return What percentage of the field is living cells. The value is rounded to one decimal place.
     */
    public double getLivingCellsCoverage() {
        double preciseCoverage = (double) livingCellsCounter / (WIDTH * HEIGHT);
        int temp = (int) (preciseCoverage * 1000.0);
        return ((double) temp) / 10.0;
    }

    /**
     * @return the color at the given coordinate. If the coordinate is outside the field, null is returned.
     */
    public Color getCellColorAt(int row, int column) {
        if (!isCoordinateInField(row, column))
            return null;
        return field[row][column].getColor();
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

    /**
     * @return The color which occurs most frequently in the 8 cells around the cell
     * at the given coordinate (row, column). If there are multiple most frequently colors,
     * one of them is returned.
     * If all cells surrounding the given cell are dead, null is returned
     */
    Color getMostFrequentlyColor(int row, int column) {
        HashMap<Color, Integer> colorAmount = new HashMap<>();

        for (int i = row - 1; i <= row + 1; i++)
            for (int k = column - 1; k <= column + 1; k++) {
                // adapt coordinates if they are outside the field boundaries
                int currentRow = getNextTorusRow(i);
                int currentColumn = getNextTorusColumn(k);

                Color currentColor = field[currentRow][currentColumn].getColor();
                // increase the amount of the current color
                if (currentColor != GofCell.DEAD_CELL_COLOR)
                    colorAmount.put(currentColor, colorAmount.getOrDefault(currentColor, 0) + 1);
            }

        // determine which color occurs most
        Color mostFrequentlyColor = null;
        int max = 0;
        for (Color color : colorAmount.keySet()) {
            if (colorAmount.get(color) > max) {
                mostFrequentlyColor = color;
                max = colorAmount.get(color);
            }
        }
        return mostFrequentlyColor;
    }

    int getAmountLivingNeighbours(int row, int column) {
        if (!isCoordinateInField(row, column))
            return 0;

        int counter = 0;
        for (int i = row - 1; i <= row + 1; i++)
            for (int k = column - 1; k <= column + 1; k++) {
                // adapt coordinates if they are outside the field boundaries
                int currentRow = getNextTorusRow(i);
                int currentColumn = getNextTorusColumn(k);

                // A neighbour cell has two characteristics:
                // - it is alive,
                // - and it has a coordinate (currentRow, currentColumn) unequal the given coordinate (row, column)
                if (field[currentRow][currentColumn].isAlive() && (currentRow != row || currentColumn != column))
                    counter++;
            }
        return counter;
    }

    GofCell[][] getField() {
        return field;
    }
}





