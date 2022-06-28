package main.view;


import main.model.Observable;

public interface Observer {
    /**
     * Observable calls this method to notify observers
     */
    void update(Observable observable);
}
