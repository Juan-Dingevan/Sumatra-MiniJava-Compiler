package exceptionhandler;

import exceptions.CompilerException;

public class ExceptionHandler {
    public static void handle(CompilerException ex) {
        System.out.println();
        setTextToRed();
        System.out.println("ERROR: " + ex.getMessage());
        setTextToWhite();
        System.out.println();
    }

    private static void setTextToWhite() {
        System.out.print("\u001B[0m");
    }

    protected static void setTextToRed() {
        System.out.print("\u001B[31m");
    }
}
