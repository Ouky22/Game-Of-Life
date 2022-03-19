package main.model;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class GofCellTest {
    @Test
    void testClone() {
        GofCell gofCell = new GofCell(5,5);
        GofCell gofCellClone = gofCell.clone();

        // both cell variables should not refer to the same object
        assertNotSame(gofCell, gofCellClone);

        // changing the color of the clone cell should not change the color of the gofCell
        gofCellClone.set(true, Color.RED);
        assertEquals(GofCell.DEAD_CELL_COLOR, gofCell.getColor());
    }
}
