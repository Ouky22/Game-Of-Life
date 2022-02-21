package main.view;


import main.controll.GameOfLiveController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private final int FIELD_WIDTH = 50;
    private final int FIELD_HEIGHT = 50;

    private final FieldPanel fieldPanel;
    private final ControlPanel controlPanel;

    private final GameOfLiveController gameOfLiveController
            = new GameOfLiveController(FIELD_HEIGHT, FIELD_WIDTH, new TimerListener());


    public MainFrame() {
        this.setLayout(new BorderLayout());

        // add fieldPanel, which contains the gameOfLive field
        this.add(fieldPanel = new FieldPanel(gameOfLiveController), BorderLayout.CENTER);

        // add ControlPanel, which is for adjusting settings for the gameOfLive
        this.add(controlPanel = new ControlPanel(gameOfLiveController, this), BorderLayout.NORTH);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(750, 750);
        this.setVisible(true);
    }

    /**
     * toggle buttons at the given positions in the field panel
     * @param coordinates of the buttons to be toggled
     */
    public void toggleButtonsInFieldPanel(ArrayList<int[]> coordinates) {
        for (int[] coordinate : coordinates)
            fieldPanel.toggleButton(coordinate);
    }

    class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int[] coordinate : gameOfLiveController.loadNextGeneration())
                fieldPanel.toggleButton(coordinate);

            controlPanel.updateGenerationTextLabel(gameOfLiveController.getGenerationCounter());
        }
    }
}
