package main.model;

import java.awt.*;

/**
 * A class representing a cell in the game of life.
 */
public class GofCell {
    private boolean alive = false;
    private Color color = Color.RED;

    /**
     * Bring the cell to life and set its color
     * @param alive Whether the cell should be alive or not
     * @param color The color the cell should have
     */
    public void set(boolean alive, Color color) {
        this.alive = alive;
        this.color = color;
    }
    
    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
