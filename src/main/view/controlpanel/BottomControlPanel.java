package main.view.controlpanel;

import main.utility.IconProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Hashtable;

public class BottomControlPanel extends JPanel {

    private final JSlider coverageSlider;
    private final JButton randomBtn;

    /**
     * Contains buttons for selecting the cell color (as value) and the buttons corresponding Color (as key).
     * So every available cell color has a corresponding button.
     */
    HashMap<Color, JButton> buttons = new HashMap<>();

    /**
     * The currently selected color
     */
    private Color selectedColor;

    /**
     * Create a Panel which contains JButtons for selecting the cell color and
     * a random cell picker to randomly select cells in the field.
     */
    public BottomControlPanel() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 64, 10));

        // create JPanel containing the elements for selecting a cell color
        JPanel colorPanel = new JPanel();
        JLabel colorLabel = new JLabel("Cell color: ");
        colorLabel.setIcon(IconProvider.getIcon(IconProvider.Icon.COLOR));
        colorLabel.setToolTipText("Select cell color");
        colorPanel.add(colorLabel);

        // create the JButtons for selecting the color.
        Color[] availableColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA};
        for (Color color : availableColors) {
            JButton btn = new JButton();
            btn.setFocusable(false);
            btn.setPreferredSize(new Dimension(20, 20));
            // get the color related to the current value of the ColorButton enum
            btn.setBackground(color);
            btn.addActionListener((e) -> selectColorButton(color));
            buttons.put(color, btn);
            colorPanel.add(btn);
        }
        selectedColor = availableColors[0];
        selectColorButton(selectedColor);


        // create JPanel containing the elements to randomly bring cells to life with a given coverage
        JPanel randomPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 12, 0));
        // JButton to randomly bring cells to life
        randomBtn = new JButton(IconProvider.getIcon(IconProvider.Icon.DICE));
        randomBtn.setFocusable(false);
        randomBtn.setBorder(BorderFactory.createEmptyBorder());
        randomBtn.setContentAreaFilled(false);
        randomBtn.setToolTipText("Select randomly cells");
        randomPanel.add(randomBtn);
        // add label
        JLabel currentCoverageLabel = new JLabel("Coverage:   5%");
        currentCoverageLabel.setPreferredSize(new Dimension(90, 15));
        randomPanel.add(currentCoverageLabel);
        // add slider for selecting what percentage of the field should be alive after the random selection of cells
        coverageSlider = new JSlider(1, 90, 5);
        // Hashtable for the slider labels containing percentages
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(1, new JLabel("1%"));
        labelTable.put(30, new JLabel("30%"));
        labelTable.put(60, new JLabel("60%"));
        labelTable.put(90, new JLabel("90%"));
        coverageSlider.setLabelTable(labelTable);
        coverageSlider.setPaintLabels(true);
        coverageSlider.setToolTipText("What % should be covered");
        coverageSlider.addChangeListener((e) -> {
            int value = coverageSlider.getValue();
            currentCoverageLabel.setText("Coverage: " + ((value < 10) ? "  " : " ") + value + "%");
        });
        randomPanel.add(coverageSlider);

        this.add(colorPanel);
        this.add(randomPanel);
    }

    public void addRandomBtnActionListener(ActionListener a) {
        randomBtn.addActionListener(a);
    }

    public int getCoverageValue() {
        return coverageSlider.getValue();
    }

    public Color getCurrentColor() {
        return selectedColor;
    }

    /**
     * Gives the button with the corresponding color a border to show that the button is selected.
     * If another button was selected before, it will no longer be selected.
     *
     * @param color The color related to the button to be selected
     */
    private void selectColorButton(Color color) {
        // unselect selected button
        buttons.get(selectedColor).setBorder(BorderFactory.createEmptyBorder());
        // set new selected color
        selectedColor = color;
        // select button related to selected color
        buttons.get(selectedColor).setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));
    }
}
