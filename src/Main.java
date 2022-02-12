import ui.gui.GUI;
import ui.tui.TUI;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        if (args.length > 0 && args[0].equals("tui"))
            new TUI();
        else
            new GUI();
    }
}
