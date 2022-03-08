package main.utility;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Class for loading icons.
 */
public final class IconProvider {
    /**
     * Path to the directory of the icons
     */
    private static final String path = "resources" + File.separator + "icons" + File.separator;

    /**
     * Represents all available icons that are used for the UI
     */
    public enum Icon {
        CLEAR,
        COLOR,
        DICE,
        NEXT,
        PREVIOUS,
        RESET,
        START,
        STOP
    }

    private IconProvider() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * @param fileName The name of the image file (without file separator)
     * @return The scaled image of the image file
     */
    private static Image getScaledImage(String fileName) {
        return new ImageIcon(path + fileName).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
    }

    /**
     * @param icon Which icon should be returned.
     * @return A scaled ImageIcon.
     */
    public static ImageIcon getIcon(Icon icon) {
        return switch (icon) {
            case CLEAR -> new ImageIcon(getScaledImage("clear.png"), "clear field");
            case COLOR -> new ImageIcon(getScaledImage("color.png"), "set cell color");
            case DICE -> new ImageIcon(getScaledImage("dice.png"), "select cells randomly");
            case NEXT -> new ImageIcon(getScaledImage("next.png"), "next generation");
            case PREVIOUS -> new ImageIcon(getScaledImage("previous.png"), "previous generation");
            case RESET -> new ImageIcon(getScaledImage("reset.png"), "reset to first generation");
            case START -> new ImageIcon(getScaledImage("start.png"), "start game of life");
            case STOP -> new ImageIcon(getScaledImage("stop.png"), "stop game of life");
        };
    }
}
