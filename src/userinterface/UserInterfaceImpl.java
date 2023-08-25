package userinterface;

import exceptionhandler.ExceptionHandler;
import exceptions.CompilerException;
import exceptions.LexicalException;
import exceptions.UnexpectedErrorException;
import lexicalanalizer.LexicalAnalyzer;
import lexicalanalizer.LexicalAnalyzerImpl;
import sourcemanager.SourceManager;
import sourcemanager.SourceManagerImpl;
import token.Token;
import utility.StringUtilities;
import utility.TokenType;

import java.io.FileNotFoundException;

public abstract class UserInterfaceImpl implements UserInterface{
    public void launch(String[] args) {
        SourceManager sourceManager = new SourceManagerImpl();
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl(sourceManager);
        ExceptionHandler exceptionHandler = new ExceptionHandler(sourceManager);

        long startingTimeInMillis = System.currentTimeMillis();

        String path = safelyGetPath(args, exceptionHandler);
        safelyOpenFile(path, sourceManager, exceptionHandler);

        Token t = null;

        while(t == null || !(t.getTokenType() == TokenType.eof)) {
            try {
                t = lexicalAnalyzer.getNextToken();
                System.out.println(t);
            } catch(LexicalException ex) {
                exceptionHandler.handleLexicalException(ex);
            } catch(CompilerException ex) {
                exceptionHandler.handleGenericException(ex);
            }
        }

        if(exceptionHandler.getExceptionsHandled() == 0) {
            StringUtilities.setTextToGreen();
            System.out.println("[SinErrores]");
            StringUtilities.setTextToWhite();
        }

        long finishingTimeInMillis = System.currentTimeMillis();
        long millisSpentCompiling = finishingTimeInMillis - startingTimeInMillis;

        System.out.println();
        System.out.println("--- Successfully compiled " + t.getLineNumber() +
                " lines in " + millisSpentCompiling +
                "ms and found " + exceptionHandler.getExceptionsHandled() + " errors. ---");
    }
    protected abstract String safelyGetPath(String[] args, ExceptionHandler exceptionHandler);
    protected void safelyOpenFile(String path, SourceManager sourceManager, ExceptionHandler exceptionHandler) {
        try {
            openFile(sourceManager, path);
        } catch(CompilerException ex) {
            exceptionHandler.handleGenericException(ex);
        }
    }
    protected static void openFile(SourceManager sourceManager, String path) throws UnexpectedErrorException {
        try {
            sourceManager.open(path);
        } catch(FileNotFoundException ex) {
            throw new UnexpectedErrorException("finding the source file: No source file found");
        }
    }
}
