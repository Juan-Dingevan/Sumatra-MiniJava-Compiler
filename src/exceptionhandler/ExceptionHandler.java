package exceptionhandler;

import exceptions.CompilerException;

public class ExceptionHandler {
    public static void handle(CompilerException ex) {
        setTextToRed();
        System.out.println("ERROR: " + ex.getMessage());
        setTextToWhite();
    }

    private static void setTextToWhite() {
        System.out.print("\u001B[0m");
    }

    protected static void setTextToRed() {
        System.out.print("\u001B[31m");
    }
}
