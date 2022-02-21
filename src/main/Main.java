package main;

import main.controller.GameOfLifeController;
import main.data.GameOfLifeField;
import main.view.MainFrame;

public class Main {
    public static void main(String[] args) {
        GameOfLifeField field = new GameOfLifeField(50, 50);
        MainFrame frame = new MainFrame();
        new GameOfLifeController(field, frame);
    }
}
