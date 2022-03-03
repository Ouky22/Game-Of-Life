package main;

import main.controller.GameOfLifeController;
import main.model.GameOfLife;
import main.view.MainFrame;

public class Main {
    public static void main(String[] args) {
        GameOfLife gof = new GameOfLife(50, 50);
        MainFrame frame = new MainFrame(gof);
        new GameOfLifeController(gof, frame);
    }
}
