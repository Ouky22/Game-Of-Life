package main.model;

import java.awt.*;

/**
 * A class representing a cell in the game of life.
 */
public class GofCell {
    public static final Color DEAD_CELL_COLOR = Color.WHITE;

    private boolean alive = false;
    private Color color = DEAD_CELL_COLOR;
    private final int row;
    private final int column;

    /**
     * Create a GofCell representing a cell in the game of life.
     *
     * @param row    The row where the cell is located
     * @param column The column where the cell is located
     */
    public GofCell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Set the life state and the color of the cell.
     *
     * @param alive Whether the cell should be alive or not
     * @param color The color the cell should have
     */
    void set(boolean alive, Color color) {
        this.alive = alive;
        this.color = color;
    }

    /**
     * Kill the cell and set the color to the default color for dead cells
     */
    void killCell() {
        this.color = DEAD_CELL_COLOR;
        this.alive = false;
    }


    public boolean isAlive() {
        return alive;
    }

    public Color getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
