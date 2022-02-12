package ui.tui;

import domain.GameOfLiveField;

public class TUI {
    static GameOfLiveField field = new GameOfLiveField(20, 20);

    public TUI() {
        field.setCellAt(5, 5, true);
        field.setCellAt(6, 6, true);
        field.setCellAt(6, 7, true);
        field.setCellAt(5, 7, true);
        field.setCellAt(4, 7, true);


        while (true) {
            printField();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            field.loadNextGeneration();
        }
    }

    private static void printField() {
        // print new lines
        for (int i = 0; i < 40; i++)
            System.out.println();

        // print field
        for (boolean[] row : field.getField()) {
            for (boolean cell : row)
                if (cell)
                    System.out.print("0 ");
                else
                    System.out.print("- ");
            System.out.println("");
        }
    }
}
