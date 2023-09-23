package userinterface;

import exceptionhandler.ExceptionHandler;
import symboltable.table.SymbolTable;

public class HardwiredUserInterface extends UserInterfaceImpl{
    private static final String PATH = "src/main/test.minijava";

    public void launch(String[] args) {
        try {
            super.launch(args);
        } catch(Exception ex) {
            ex.printStackTrace();
            System.out.println(SymbolTable.getInstance().toString());
        }
    }

    @Override
    protected String safelyGetPath(String[] args, ExceptionHandler exceptionHandler) {
        return PATH;
    }
}
