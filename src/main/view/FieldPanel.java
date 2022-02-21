package main.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class FieldPanel extends JPanel {
    private JButton[][] jButtons;

    public void init(int rows, int columns, ActionListener buttonListener) {
        this.setLayout(new GridLayout(rows, columns));

        jButtons = new JButton[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                JButton btn = new JButton();
                btn.setFocusable(false);
                btn.setBackground(Color.WHITE);
                btn.setActionCommand(createActionCommandString(row, col, false));
                btn.addActionListener(buttonListener);
                this.add(btn);
                jButtons[row][col] = btn;
            }
        }
    }

    /**
     * toggle the life status of the button and the color at the given coordinate (row, column)
     *
     * @param coordinate of button which should be toggled
     */
    public void toggleButton(int[] coordinate) {
        int row = coordinate[0];
        int column = coordinate[1];
        toggleButton(row, column);
    }

    /**
     * toggle the life status of the button and the color at the given row and column
     *
     * @param row    of button which should be toggled
     * @param column of button which should be toggled
     */
    public void toggleButton(int row, int column) {
        JButton btn = jButtons[row][column];
        boolean alive = btn.getActionCommand().split(",")[0].equals("alive");

        // toggle color of button
        if (alive) // cell dies
            btn.setBackground(Color.WHITE);
        else // cell becomes alive
            btn.setBackground(Color.RED);

        // toggle life status in actionCommand of button
        btn.setActionCommand(createActionCommandString(row, column, !alive));
    }

    private String createActionCommandString(int row, int column, boolean alive) {
        return (alive ? "alive" : "dead") + "," + row + "," + column;
    }
}
