package main.model;

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
}
