package main.model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class GameOfLifeFieldTest {

    @Test
    void testSetCellAt() {
        GameOfLifeField gameOfLifeField = new GameOfLifeField(100, 100);

        // at the beginning every cell in the field is dead (false)
        for (GofCell[] row : gameOfLifeField.getField())
            for (GofCell b : row)
                assertFalse(b.isAlive());

        // pass coordinates that are outside the field
        gameOfLifeField.setCellAt(-1, 999, true);
        // so there is still no cell alive
        for (GofCell[] row : gameOfLifeField.getField())
            for (GofCell b : row)
                assertFalse(b.isAlive());

        // bring cell at row 0 and column 0 to life
        gameOfLifeField.setCellAt(0, 0, true);
        // only at row 0 and column 0 there should be a living cell
        GofCell[][] booleanField = gameOfLifeField.getField();
        for (int row = 0; row < booleanField.length; row++)
            for (int col = 0; col < booleanField[row].length; col++)
                if (row == 0 && col == 0)
                    assertTrue(booleanField[row][col].isAlive());
                else
                    assertFalse(booleanField[row][col].isAlive());

        // bring cell at row 0 and column 0 to life
        gameOfLifeField.setCellAt(0, 0, false);
        // so again there is no cell alive
        for (GofCell[] row : gameOfLifeField.getField())
            for (GofCell b : row)
                assertFalse(b.isAlive());

    }

    @Test
    void testGetAmountLivingNeighbours() {
        GameOfLifeField gameOfLifeField = new GameOfLifeField(100, 100);
        // bring cells to life. It looks like this:
        // ----O- -> cell 1
        // --OO-- -> cell 2 and 3 (from left to right)
        // ------ -> no cells
        // --O--- -> cell 4
        gameOfLifeField.setCellAt(4, 7, true); // cell 1
        gameOfLifeField.setCellAt(5, 5, true); // cell 2
        gameOfLifeField.setCellAt(5, 6, true); // cell 3
        gameOfLifeField.setCellAt(7, 5, true); // cell 4

        // cell 1 has one neighbour (cell 3)
        assertEquals(1, gameOfLifeField.getAmountLivingNeighbours(4, 7));

        // cell 2 has one neighbours (cell 3)
        assertEquals(1, gameOfLifeField.getAmountLivingNeighbours(5, 5));

        // cell 3 has two neighbours (cell 1 and cell 3)
        assertEquals(2, gameOfLifeField.getAmountLivingNeighbours(5, 6));

        // cell 4 has no neighbours
        assertEquals(0, gameOfLifeField.getAmountLivingNeighbours(7, 5));
    }

    @Test
    void testIsCoordinateInField() {
        int width = 10;
        int height = 10;
        GameOfLifeField gameOfLifeField = new GameOfLifeField(height, width);

        // test coordinates that are outside the field
        assertFalse(gameOfLifeField.isCoordinateInField(-1, -2));
        assertFalse(gameOfLifeField.isCoordinateInField(15, 0));
        assertFalse(gameOfLifeField.isCoordinateInField(0, width));
        assertFalse(gameOfLifeField.isCoordinateInField(height, 0));

        // test coordinates that are inside the field
        assertTrue(gameOfLifeField.isCoordinateInField(0, 0));
        assertTrue(gameOfLifeField.isCoordinateInField(0, width - 1));
        assertTrue(gameOfLifeField.isCoordinateInField(height - 1, 0));
        assertTrue(gameOfLifeField.isCoordinateInField(height - 1, width - 1));
        assertTrue(gameOfLifeField.isCoordinateInField(7, 5));
    }

    @Test
    void testLoadNextGeneration() throws FileNotFoundException {
        GameOfLifeField gameOfLifeField = new GameOfLifeField(100, 100);
        // contains the generations, which each have a list of coordinates, which each have a row and a column
        ArrayList<ArrayList<int[]>> generations = getTestCoordinateGeneration();

        // bring start cells to live
        for (int[] coordinate : generations.get(0))
            gameOfLifeField.setCellAt(coordinate[0], coordinate[1], true);

        // go through every generation
        for (ArrayList<int[]> generation : generations) {
            // go through every coordinate per generation
            for (int[] coordinate : generation) {
                int row = coordinate[0];
                int column = coordinate[1];
                // the cells at the given coordinate should be alive
                assertTrue(gameOfLifeField.getField()[row][column].isAlive());
            }

            // load next generation
            gameOfLifeField.getNextGeneration();
        }
    }

    @Test
    void testKillAllCells() {
        int width = 10;
        int height = 10;
        GameOfLifeField gameOfLifeField = new GameOfLifeField(height, width);

        // bring cells to life...
        int[][] cellPositions = {{0, 0}, {height - 1, width - 1}, {height - 1, 0}};
        for (int[] coordinate : cellPositions)
            gameOfLifeField.setCellAt(coordinate[0], coordinate[1], true);
        // ...and kill them
        gameOfLifeField.killAllCells();

        // all cells in the field should be dead
        for (GofCell[] row : gameOfLifeField.getField())
            for (GofCell col : row)
                assertFalse(col.isAlive());
    }

    @Test
    void testKillAllCellsExceptOf() {
        int width = 10;
        int height = 10;
        GameOfLifeField gameOfLifeField = new GameOfLifeField(height, width);

        // bring cells to life...
        ArrayList<int[]> sparedCells = new ArrayList<>();
        sparedCells.add(new int[]{0, 0});
        sparedCells.add(new int[]{height - 1, width - 1});
        int[][] cellsToBeKilled = {{5, 5}, {0, width - 1}};
        for (int[] coordinate : sparedCells)
            gameOfLifeField.setCellAt(coordinate[0], coordinate[1], true);
        for (int[] coordinate : cellsToBeKilled)
            gameOfLifeField.setCellAt(coordinate[0], coordinate[1], true);

        // ...and kill them except of the spared cells
        gameOfLifeField.killAllCellsExceptOf(sparedCells);

        // so all cells of the field except of the spared cells should be dead
        for (int row = 0; row < gameOfLifeField.getField().length; row++)
            for (int col = 0; col < gameOfLifeField.getField()[row].length; col++) {
                // determine if the current cell is a cell that should have been spared or killed
                boolean isSparedCellPosition = false;
                for (int[] sparedCellPos : sparedCells)
                    if (Arrays.equals(sparedCellPos, new int[]{row, col})) {
                        isSparedCellPosition = true;
                        break;
                    }

                // if cell should have been spared, it should be still alive...
                if (isSparedCellPosition)
                    assertTrue(gameOfLifeField.getField()[row][col].isAlive());
                else // ...otherwise it should be dead
                    assertFalse(gameOfLifeField.getField()[row][col].isAlive());
            }
    }

    @Test
    void testGetNextRowAndColumn() {
        int width = 10;
        int height = 10;
        GameOfLifeField gameOfLifeField = new GameOfLifeField(height, width);

        // the field should behave like a torus, so if the row or column index is outside the field boundaries, return
        // the right row

        // row <= -1 => row = height - 1
        assertEquals(height - 1, gameOfLifeField.getNextTorusRow(-1));
        // row >= width => row = 0
        assertEquals(0, gameOfLifeField.getNextTorusRow(height));

        // column >= -1 => column = width - 1
        assertEquals(width - 1, gameOfLifeField.getNextTorusColumn(-1));
        // column >= width => column = 0
        assertEquals(0, gameOfLifeField.getNextTorusColumn(width));
    }

    @Test
    void testGetLivingCellsCoverage() {
        GameOfLifeField field = new GameOfLifeField(11, 11);
        // no cell is alive, so the coverage must be 0
        assertEquals(0, field.getLivingCellsCoverage());

        // bring all cells in the first row (index = 0) to life (11 cells)
        for (int i = 0; i < 11; i++)
            field.setCellAt(0, i, true);

        // there are 11 * 11 = 121 cells in the field. 11 of them are alive.
        // So the coverage must be 11 / 121, which is about 0.09 = 9 %
        assertEquals(9, field.getLivingCellsCoverage());

        // bring 9 more cells in row 9 to life, so 20 cells are alive
        for (int i = 0; i < 9; i++)
            field.setCellAt(9, i, true);
        // the coverage must 20 / 121, which is about 0.165 = 16.5 %
        assertEquals(16.5, field.getLivingCellsCoverage());

        // kill all cells except of 2 cells
        ArrayList<int[]> sparedCells = new ArrayList<>();
        sparedCells.add(new int[]{0, 0});
        sparedCells.add(new int[]{0, 5});
        field.killAllCellsExceptOf(sparedCells);
        // so the coverage must be 2 / 121, which is about 0.016 = 1.6 %
        assertEquals(1.6, field.getLivingCellsCoverage());

        // kill all cells
        field.killAllCells();
        // so the coverage must be 0 %
        assertEquals(0, field.getLivingCellsCoverage());
    }

    // first dimension: generation
    // second dimension: coordinates
    // third dimension: coordinate (column and row)
    private ArrayList<ArrayList<int[]>> getTestCoordinateGeneration() throws FileNotFoundException {
        ArrayList<String> csvRows = loadCsvFile("resources/testPositions.csv");
        ArrayList<ArrayList<int[]>> generations = new ArrayList<>();

        // extract coordinates from csv file
        for (String csvRow : csvRows) {
            ArrayList<int[]> coordinates = new ArrayList<>();
            String[] coordinateStrings = csvRow.split(",");
            for (String coordinateString : coordinateStrings) {
                String[] coordinate = coordinateString.split("\\.");
                int row = Integer.parseInt(coordinate[0]);
                int column = Integer.parseInt(coordinate[1]);
                coordinates.add(new int[]{row, column});
            }
            generations.add(coordinates);
        }
        return generations;
    }

    private ArrayList<String> loadCsvFile(String path) throws FileNotFoundException {
        ArrayList<String> rows = new ArrayList<>();

        Scanner sc = new Scanner(new File(path));
        while (sc.hasNextLine()) {
            String row = sc.nextLine();
            rows.add(row);
        }
        sc.close();

        return rows;
    }
}
