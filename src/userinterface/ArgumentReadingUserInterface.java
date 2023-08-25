package userinterface;

import exceptionhandler.ExceptionHandler;
import exceptions.CompilerCallException;
import exceptions.CompilerException;
import exceptions.LexicalException;
import lexicalanalizer.LexicalAnalyzer;
import lexicalanalizer.LexicalAnalyzerImpl;
import sourcemanager.SourceManager;
import sourcemanager.SourceManagerImpl;
import token.Token;
import utility.TokenType;

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
