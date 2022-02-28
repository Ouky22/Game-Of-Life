package main.controller;

import main.data.GameOfLifeField;
import main.view.ControlPanel;
import main.view.FieldPanel;
import main.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Mediator between the logic and the UI
 */
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
        // --- initialize fieldPanel with values from gameOfLifeField and pass ActionListener for buttons
        fieldPanel.init(gameOfLifeField.getHeight(), gameOfLifeField.getWidth(), (e) -> {
            if (e.getSource() instanceof JButton btn) {
                boolean alive = btn.getActionCommand().split(",")[0].equals("alive");
                int row = Integer.parseInt(btn.getActionCommand().split(",")[1]);
                int column = Integer.parseInt(btn.getActionCommand().split(",")[2]);

                fieldPanel.toggleButton(row, column);
                setCellAt(row, column, !alive);
            }
        });

        // --- set ActionListener of ControlPanel
        // start/restart button
        controlPanel.addStartRestartBtnActionListener((e) -> {
            if (e.getSource() instanceof JButton startRestartButton)
                if (isGameOfLiveRunning()) {
                    stopGameOfLive();
                    startRestartButton.setText("Start");
                } else {
                    startGameOfLive();
                    startRestartButton.setText("Stop");
                }
        });

        // jump button
        controlPanel.addJumpButtonActionListener((e) -> triggerNextGeneration());

        // reset/clear button
        controlPanel.addResetClearBtnActionListener((e) -> {
            if (e.getSource() instanceof JButton resetClearButton)
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
        controlPanel.addDelaySliderChangeListener((e) -> {
            if (e.getSource() instanceof JSlider delaySlider) {
                int currentSpeed = delay;
                int currentValue = delaySlider.getValue();
                // only change speed when the slider has a final result
                // or if the difference between current slider value and speed is greater than 3
                if (delaySlider.getValueIsAdjusting() || Math.abs(currentValue - currentSpeed) > 3)
                    setDelay((delaySlider.getMaximum() - currentValue) * 100);
            }
        });

        mainFrame.setVisible(true);
    }


    /**
     * Loads the next generation of the game of life and increments the generation counter
     *
     * @return coordinates of cells which got a new life state
     */
    public ArrayList<int[]> loadNextGeneration() {
        gameOfLifeField.incrementGenerationCounter();
        return gameOfLifeField.loadNextGeneration();
    }

    /**
     * Set one cell alive or not alive in the gameOfLive field at the given row and column.
     * If it is the first generation, the list containing the cell positions from the first generation will be updated.
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

    /**
     * Starts the timer so that it is firing events periodically with the given delay causing to load the next generation
     */
    public void startGameOfLive() {
        timer.setRepeats(true);
        timer.restart();
    }

    /**
     * Stopps the timer so that it does not fire events causing to load the next generation
     */
    public void stopGameOfLive() {
        timer.stop();
    }

    /**
     * Triggers timer so the next generation will be generated and drawn.
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
     * @return coordinates of cells which got a new life state (deed or alive)
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
     * Sets the delay between each Generation
     *
     * @param delay in milliseconds
     */
    public void setDelay(int delay) {
        this.delay = delay;
        this.timer.setDelay(delay);
    }

    /**
     * Updates the text of the generationTextLabel to the current generation counter
     */
    public void updateGenerationCounterText() {
        controlPanel.setGenerationTextLabel(gameOfLifeField.getGenerationCounter());
    }

    /**
     * ActionListener, which executes all the necessary steps for loading the next generation if the timer fires an event
     */
    class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int[] coordinate : loadNextGeneration())
                fieldPanel.toggleButton(coordinate);
            updateGenerationCounterText();

            controlPanel.setResetClearBtnText("Reset");
        }
    }
}
