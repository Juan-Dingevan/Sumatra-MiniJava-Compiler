package main;
import userinterface.HardwiredUserInterface;
import userinterface.UserInterface;

public class Main {
    public static void main(String[] args) {
        UserInterface ui = new HardwiredUserInterface();
        ui.launch(args);
    }
}