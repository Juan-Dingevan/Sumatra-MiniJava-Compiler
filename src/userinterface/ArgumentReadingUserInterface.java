package userinterface;

import exceptionhandler.ExceptionHandler;
import exceptions.general.CompilerCallException;
import exceptions.general.CompilerException;

public class ArgumentReadingUserInterface extends UserInterfaceImpl {
    protected void validateCall(String[] args) throws CompilerCallException {
        if(args.length != 1)
            throw new CompilerCallException(args.length);
    }

    @Override
    protected String safelyGetPath(String[] args, ExceptionHandler exceptionHandler) {
        try {
            validateCall(args);
        } catch(CompilerException ex) {
            exceptionHandler.handleGenericException(ex);
            return "";
        }

        return args[0];
    }
}
