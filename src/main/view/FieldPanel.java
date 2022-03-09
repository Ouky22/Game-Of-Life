package main.view;

import main.model.GameOfLife;
import main.model.GofCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * A JPanel which can display all the cells of the game of life in a grid field
 * consisting of JButtons which represent cells.
 */
public class FieldPanel extends JPanel implements Observer {
    private final JButton[][] jButtons;
    private final GameOfLife gameOfLife;

    public FieldPanel(GameOfLife gameOfLife) {
        this.gameOfLife = gameOfLife;
        gameOfLife.register(this);

        int rows = gameOfLife.getFieldHeight();
        int columns = gameOfLife.getFieldWidth();

        this.setLayout(new GridLayout(rows, columns));

        jButtons = new JButton[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                JButton btn = new JButton();
                btn.setFocusable(false);
                btn.setBackground(GofCell.DEAD_CELL_COLOR);
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

    private String createActionCommandString(int row, int column, boolean alive) {
        return (alive ? "alive" : "dead") + "," + row + "," + column;
    }

    @Override
    public void update() {
        // get the positions of the cells which got a new life state and update field panel
        for (int[] position : gameOfLife.clearCellsToBeUpdated()) {
            int row = position[0];
            int col = position[1];
            JButton btn = jButtons[row][col];

            btn.setBackground(gameOfLife.getCellColorAt(row, col));

            // toggle life state in the action command of the button
            boolean alive = btn.getActionCommand().split(",")[0].equals("alive");
            btn.setActionCommand(createActionCommandString(row, col, !alive));
        }
    }
}



























