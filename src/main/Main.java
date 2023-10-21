package main;
import userinterface.ArgumentReadingUserInterface;
import userinterface.HardwiredUserInterface;
import userinterface.StageThreeUserInterface;
import userinterface.UserInterface;

public class Main {
    private static final UserInterface TEST_INTERFACE = new HardwiredUserInterface();
    private static final UserInterface REAL_INTERFACE = new ArgumentReadingUserInterface();

    private static final UserInterface OLD_INTERFACE = new StageThreeUserInterface();

    public static void main(String[] args) {
        UserInterface ui = REAL_INTERFACE;
        ui.launch(args);
    }
}