package main.view;

import main.model.GameOfLife;

public interface Observer {
    /**
     * Observable calls this method to notify observers
     */
    void update(GameOfLife gameOfLife);
}
