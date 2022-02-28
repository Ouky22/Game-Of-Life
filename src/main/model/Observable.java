package main.model;

import main.view.Observer;

public interface Observable {
    /**
     * Register an observer
     * @param observer Observer to be registered
     */
    void register(Observer observer);

    /**
     * Unregister an observer
     * @param observer Observer to be unregistered
     */
    void unregister(Observer observer);

    /**
     * notify all registered observers
     */
    void notifyObservers();
}
