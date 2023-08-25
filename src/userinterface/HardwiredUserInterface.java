package userinterface;

import exceptionhandler.ExceptionHandler;

public class HardwiredUserInterface extends UserInterfaceImpl{
    private static final String PATH = "src/main/test.minijava";
    @Override
    protected String safelyGetPath(String[] args, ExceptionHandler exceptionHandler) {
        return PATH;
    }
}
