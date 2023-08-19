package exceptionhandler;

import exceptions.CompilerException;

public class ExceptionHandler {
    public static void handle(CompilerException ex) {
        System.out.println("ERROR: " + ex.getMessage());
    }
}
