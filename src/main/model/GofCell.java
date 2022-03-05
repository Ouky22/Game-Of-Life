package main.model;

import java.awt.*;

/**
 * A class representing a cell in the game of life.
 */
public class GofCell {
    private boolean alive = false;
    private Color color = Color.RED;


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
