package test.model;

import main.model.GameOfLife;
import main.model.GofCell;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class GameOfLifeTest {
    @Test
    void testResetToFirstGeneration() {
        GameOfLife gof = new GameOfLife(10, 10);

        // bring cells to life. They form the following figure:
        // ----O-- -> cell 1
        // --O-O-- -> cell 2 and 3 (left to right)
        // ---OO-- -> cell 4 and 5 (left to right)
        HashMap<int[], Color> firstGeneration = new HashMap<>();
        firstGeneration.put(new int[]{1, 3}, Color.RED); // cell 1
        firstGeneration.put(new int[]{2, 1}, Color.GREEN); // cell 2
        firstGeneration.put(new int[]{2, 3}, Color.PINK); // cell 3
        firstGeneration.put(new int[]{3, 2}, Color.YELLOW); // cell 4
        firstGeneration.put(new int[]{3, 3}, Color.BLUE); // cell 5
        // bring them to life
        for (int[] pos : firstGeneration.keySet())
            gof.reviveCellAt(pos[0], pos[1], firstGeneration.get(pos));

        // load the next 3 generations and make a reset to the first generation
        for (int i = 0; i < 3; i++)
            gof.loadNextGeneration();
        gof.resetToFirstGeneration();

        // the generation counter should be reset to 1
        assertEquals(1, gof.getGenerationCounter());

        // now only the cells from the first generation should be alive
        for (int row = 0; row < gof.getFieldHeight(); row++) {
            for (int col = 0; col < gof.getFieldWidth(); col++) {
                Color currentCellColor = GofCell.DEAD_CELL_COLOR;

                // If the current position is a position of a first generation cell,
                // the color should be the color of this first generation cell.
                // Otherwise, the cell should be dead and so the color is DEAD_CELL_COLOR.
                for (int[] pos : firstGeneration.keySet())
                    if (Arrays.equals(pos, new int[]{row, col}))
                        currentCellColor = firstGeneration.get(pos);

                assertEquals(currentCellColor, gof.getCellColorAt(row, col));
            }
        }
    }

    @Test
    void testResetGameOfLife() {
        GameOfLife gof = new GameOfLife(10, 10);

        // bring a few cells to life
        for (int i = 0; i < 5; i++)
            gof.reviveCellAt(0, i, Color.RED);

        gof.resetGameOfLife();

        // because the game of life was reset, no cell should be alive
        for (int row = 0; row < gof.getFieldHeight(); row++)
            for (int col = 0; col < gof.getFieldWidth(); col++)
                assertEquals(GofCell.DEAD_CELL_COLOR, gof.getCellColorAt(row, col));

        // the generation counter should be 1
        assertEquals(1, gof.getGenerationCounter());
    }

    @Test
    void testGoToGeneration() {
        GameOfLife gof = new GameOfLife(10, 10);
        Color cellColor = Color.RED;

        int[][] odd_figure = {
                new int[]{2, 2},
                new int[]{2, 3},
                new int[]{2, 4}};
        // This creates a repeating pattern, in which two outer cells "rotate" around a middle cell.
        // Every generation with an odd number has the following pattern (odd_figure):
        // -------
        // --OOO--
        // -------


        int[][] even_figure = {
                new int[]{1, 3},
                new int[]{2, 3},
                new int[]{3, 3}};
        // Every Generation with an even number forms the following pattern (even_figure):
        // ---O---
        // ---O---
        // ---O---


        // bring cells of odd_figure to life
        for (int[] pos : odd_figure)
            gof.reviveCellAt(pos[0], pos[1], cellColor);


        // going to generation 22 results in the even_figure
        gof.goToGeneration(22);
        assertTrue(areGivenCellsAlive(gof, even_figure, cellColor));
        // and the generation counter should be 22
        assertEquals(22, gof.getGenerationCounter());


        // going back to generation 3 should result in the odd_figure
        gof.goToGeneration(3);
        assertTrue(areGivenCellsAlive(gof, odd_figure, cellColor));
        // and the generation counter should be 3
        assertEquals(3, gof.getGenerationCounter());


        // passing a negative number shouldn't change anything, so the living cells still form the odd_figure
        gof.goToGeneration(-10);
        assertTrue(areGivenCellsAlive(gof, odd_figure, cellColor));
        // and the generation counter should still be 3
        assertEquals(3, gof.getGenerationCounter());


        // because 0 is not a valid generation number, passing it to the gotTo Method shouldn't change anything,
        // so the living cells still form the odd_figure
        gof.goToGeneration(0);
        assertTrue(areGivenCellsAlive(gof, odd_figure, cellColor));
        assertEquals(3, gof.getGenerationCounter());
    }

    @Test
    void testManualManipulations() {
        GameOfLife gof = new GameOfLife(10, 10);
        Color cellColor = Color.RED;

        int[][] odd_figure = {
                new int[]{2, 2},
                new int[]{2, 3},
                new int[]{2, 4}};
        // This creates a repeating pattern, in which two outer cells "rotate" around a middle cell.
        // Every generation with an odd number has the following pattern (odd_figure):
        // -------
        // --OOO--
        // -------

        // bring cells of odd_figure to life
        for (int[] pos : odd_figure)
            gof.reviveCellAt(pos[0], pos[1], cellColor);

        // go to the third generation (which forms odd_figure) and simulate a manual change on a cell,
        // so that this figure is created:
        // ---O--- this cell is revived in yellow
        // --OOO--
        // -------
        gof.loadNextGeneration();
        gof.loadNextGeneration();
        gof.reviveCellAt(1, 3, Color.YELLOW);

        // this leads to this figure in the 4th generation:
        // --OOO-- only the middle cell is yellow
        // --OOO--
        // ---O---
        gof.loadNextGeneration();

        // going back to the second generation and then going to the third generation should bring the cell
        // brought to life manually in yellow back to life
        gof.goToGeneration(2);
        gof.goToGeneration(3);
        assertEquals(gof.getCellColorAt(1, 3), Color.YELLOW);

        // going to generation 4 leads to the figure from generation 4
        gof.loadNextGeneration();
        // check if the top row of cells has the following colors: red, yellow and red
        assertEquals(gof.getCellColorAt(1,2), Color.RED);
        assertEquals(gof.getCellColorAt(1,3), Color.YELLOW);
        assertEquals(gof.getCellColorAt(1,4), Color.RED);
    }

    /**
     * This is a helper method, which returns true if only the cells at the given positions
     * are alive in the given field and have the given color.
     *
     * @param gof       The GameOfLifeField instance
     * @param positions The positions of cells that should be alive
     * @param color     What color the living cells should have.
     * @return true if only the cells at the given positions are alive and have the given color
     */
    private boolean areGivenCellsAlive(GameOfLife gof, int[][] positions, Color color) {
        for (int row = 0; row < gof.getFieldHeight(); row++) {
            for (int col = 0; col < gof.getFieldWidth(); col++) {
                Color currentCellColor = GofCell.DEAD_CELL_COLOR;

                // if the current cell position is one of the given positions, set the current color to the given color
                for (int[] pos : positions)
                    if (Arrays.equals(pos, new int[]{row, col})) {
                        currentCellColor = color;
                        break;
                    }
                // the currentCellColor should match the color at the current position
                if (currentCellColor != gof.getCellColorAt(row, col))
                    return false;
            }
        }
        return true;
    }
}




















