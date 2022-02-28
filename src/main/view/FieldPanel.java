package main.view;

import main.model.GameOfLifeField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * A JPanel which can display all the cells of the game of life in a grid field
 * consisting of JButtons which represent cells.
 */
public class FieldPanel extends JPanel implements Observer {
    private final JButton[][] jButtons;
    private final GameOfLifeField gameOfLifeField;

    public FieldPanel(GameOfLifeField gameOfLifeField) {
        this.gameOfLifeField = gameOfLifeField;
        gameOfLifeField.register(this);

        int rows = gameOfLifeField.getHeight();
        int columns = gameOfLifeField.getWidth();

        this.setLayout(new GridLayout(rows, columns));

        jButtons = new JButton[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                JButton btn = new JButton();
                btn.setFocusable(false);
                btn.setBackground(Color.WHITE);
                btn.setActionCommand(createActionCommandString(row, col, false));
                this.add(btn);
                jButtons[row][col] = btn;
            }
        }
    }

    /**
     * add ActionListener to every button in the field. Every Button is a representation of a cell.
     *
     * @param buttonListener ActionListener to be added
     */
    public void addButtonActionListener(ActionListener buttonListener) {
        for (JButton[] buttonRow : jButtons)
            for (JButton button : buttonRow)
                button.addActionListener(buttonListener);
    }

    /**
     * Toggle the color indicating the life state of the corresponding cell of the button
     * at the given coordinate (row, column)
     *
     * @param coordinate of button which should be toggled (row, column)
     */
    private void toggleButton(int[] coordinate) {
        int row = coordinate[0];
        int column = coordinate[1];
        toggleButton(row, column);
    }

    /**
     * Toggle the color indicating the life state of the corresponding cell of the button
     * at the given row and column
     *
     * @param row    of button which should be toggled
     * @param column of button which should be toggled
     */
    private void toggleButton(int row, int column) {
        JButton btn = jButtons[row][column];
        boolean alive = btn.getActionCommand().split(",")[0].equals("alive");

        // toggle color of button
        if (alive) // cell dies
            btn.setBackground(Color.WHITE);
        else // cell becomes alive
            btn.setBackground(Color.RED);

        // toggle life state in actionCommand of button
        btn.setActionCommand(createActionCommandString(row, column, !alive));
    }

    private String createActionCommandString(int row, int column, boolean alive) {
        return (alive ? "alive" : "dead") + "," + row + "," + column;
    }

    @Override
    public void update() {
        // get the positions of the cells which got a new life state and update field panel
        for (int[] position : gameOfLifeField.clearCellsToBeUpdated())
            toggleButton(position);
    }
}
