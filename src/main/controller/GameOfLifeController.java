package main.controller;

import main.data.GameOfLifeField;
import main.view.ControlPanel;
import main.view.FieldPanel;
import main.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameOfLifeController {
    private final GameOfLifeField gameOfLifeField;
    private final MainFrame mainFrame;
    private final FieldPanel fieldPanel;
    private final ControlPanel controlPanel;

    private int delay = 1000;
    private final Timer timer;


    // contains the positions of the cells in the first generation
    private final ArrayList<int[]> firstGenCellPositions = new ArrayList<>();

    public GameOfLifeController(GameOfLifeField field, MainFrame frame) {
        this.gameOfLifeField = field;
        this.mainFrame = frame;
        this.fieldPanel = frame.getFieldPanel();
        this.controlPanel = frame.getControlPanel();

        // init ui with values from gameOfLifeField and add actionListener
        init();

        // initialize timer
        this.timer = new Timer(delay, new TimerListener());
        timer.setInitialDelay(50);
    }

    private void init() {
        // --- initialize fieldPanel with values from gameOfLifeField and pass ButtonActionListener
        fieldPanel.init(gameOfLifeField.getHeight(), gameOfLifeField.getWidth(), new ButtonListener());

        // --- set ActionListener of ControlPanel
        // start/restart button
        JButton restartBtn = controlPanel.getStartRestartBtn();
        restartBtn.addActionListener((e) -> {
            if (isGameOfLiveRunning()) {
                stopGameOfLive();
                restartBtn.setText("Start");
            } else {
                startGameOfLive();
                restartBtn.setText("Stop");
            }
        });

        // jump button
        controlPanel.getJumpButton().addActionListener((e) -> triggerNextGeneration());

        // reset/clear button
        JButton resetClearButton = controlPanel.getResetClearBtn();
        resetClearButton.addActionListener((e) -> {
            // if the current generation is not the first generation...
            if (gameOfLifeField.getGenerationCounter() > 1) {
                //... reset to the first generation and update fieldPanel...
                for (int[] coordinate : resetToFirstGeneration())
                    fieldPanel.toggleButton(coordinate);

                // update text of resetClearButton because now its functionality is to clear the field
                resetClearButton.setText("Clear");
                // update generationTextLabel
                updateGenerationCounterText();
            } else {
                // ...otherwise kill all cells to clear the field
                for (int[] coordinate : killAllCells())
                    fieldPanel.toggleButton(coordinate);
            }
        });

        // delay slider
        JSlider slider = controlPanel.getDelaySlider();
        slider.addChangeListener((e) -> {
            int currentSpeed = delay;
            int currentValue = slider.getValue();
            // only change speed when the slider has a final result
            // or if the difference between current slider value and speed is greater than 3
            if (slider.getValueIsAdjusting() || Math.abs(currentValue - currentSpeed) > 3)
                setDelay((slider.getMaximum() - currentValue) * 100);
        });

        mainFrame.setVisible(true);
    }

    /**
     * loads the next generation of the game of life
     *
     * @return coordinates of cells which got a new life status
     */
    public ArrayList<int[]> loadNextGeneration() {
        gameOfLifeField.incrementGenerationCounter();
        return gameOfLifeField.loadNextGeneration();
    }

    /**
     * set one cell in the gameOfLive field at the given row and column alive or not alive
     *
     * @param row    row of the cell
     * @param column column of the cell
     * @param alive  whether the cell should be alive or dead
     */
    public void setCellAt(int row, int column, boolean alive) {
        gameOfLifeField.setCellAt(row, column, alive);

        // if it is the first generation, the positions of the living cells
        // needs to be updated in the firstGenCellPositions list. Otherwise, return.
        if (gameOfLifeField.getGenerationCounter() != 1)
            return;

        // if a cell is brought to life, add it to firstGenCellPositions
        if (alive) {
            firstGenCellPositions.add(new int[]{row, column});
            return;
        }

        // if a cell is killed, remove it from firstGenCellPositions
        for (int i = 0; i < firstGenCellPositions.size(); i++)
            if (firstGenCellPositions.get(i)[0] == row && firstGenCellPositions.get(i)[1] == column) {
                firstGenCellPositions.remove(i);
                break;
            }
    }

    public void startGameOfLive() {
        timer.setRepeats(true);
        timer.restart();
    }

    public void stopGameOfLive() {
        timer.stop();
    }

    /**
     * trigger timer so the next generation will be generated and drawn.
     * If the timer is running before this method call, it will continue afterwards.
     * If the timer is not running before this method call,
     * it will only trigger the timer once and then stops again.
     */
    public void triggerNextGeneration() {
        // if the timer is not running, the timer should fire only once (until it is started again)
        if (!timer.isRunning())
            timer.setRepeats(false);

        // restart timer so that the next event is fired after the initial delay of timer
        // causing the next generation to be created and displayed
        timer.restart();
    }

    /**
     * Kills all cells in the field except the cells from the first generation.
     * If cells from the first generation are dead, they are brought back to live.
     *
     * @return coordinates of cells which got a new life status (deed or alive)
     */
    public ArrayList<int[]> resetToFirstGeneration() {
        // convert the startCellPositions list to array
        int[][] startPositionsArray = new int[firstGenCellPositions.size()][2];
        for (int row = 0; row < firstGenCellPositions.size(); row++)
            startPositionsArray[row] = firstGenCellPositions.get(row);

        // kill all cells in the field except the cell(s) from the first generation (in startCellPositions)
        ArrayList<int[]> toggledCells = gameOfLifeField.killAllCellsExceptOf(startPositionsArray);

        // if a cell from the first generation is dead, bring it back to life
        for (int[] coordinate : firstGenCellPositions)
            if (!gameOfLifeField.isCellAliveAt(coordinate)) {
                setCellAt(coordinate[0], coordinate[1], true);
                toggledCells.add(coordinate);
            }
        gameOfLifeField.resetGenerationCounter();

        return toggledCells;
    }

    /**
     * Kills all cells in the field
     *
     * @return coordinates of cells which got killed
     */
    public ArrayList<int[]> killAllCells() {
        firstGenCellPositions.clear();
        return gameOfLifeField.killAllCells();
    }

    public boolean isGameOfLiveRunning() {
        return timer.isRunning();
    }

    /**
     * sets the delay between each Generation
     *
     * @param delay in milliseconds
     */
    public void setDelay(int delay) {
        this.delay = delay;
        this.timer.setDelay(delay);
    }

    /**
     * updates the text of the generationTextLabel to the current generation counter
     */
    public void updateGenerationCounterText() {
        controlPanel.getGenerationTextLabel().setText("Generation: " + gameOfLifeField.getGenerationCounter());
    }

    class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int[] coordinate : loadNextGeneration())
                fieldPanel.toggleButton(coordinate);
            updateGenerationCounterText();
            controlPanel.getResetClearBtn().setText("Reset");
        }
    }

    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JButton btn) {
                boolean alive = btn.getActionCommand().split(",")[0].equals("alive");
                int row = Integer.parseInt(btn.getActionCommand().split(",")[1]);
                int column = Integer.parseInt(btn.getActionCommand().split(",")[2]);

                fieldPanel.toggleButton(row, column);
                setCellAt(row, column, !alive);
            }
        }
    }
}
