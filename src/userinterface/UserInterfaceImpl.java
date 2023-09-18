package userinterface;

import exceptionhandler.ExceptionHandler;
import exceptions.general.CompilerException;
import exceptions.general.UnexpectedErrorException;
import exceptions.lexical.LexicalException;
import exceptions.semantical.SemanticException;
import exceptions.syntax.SyntaxException;
import lexicalanalizer.LexicalAnalyzer;
import lexicalanalizer.LexicalAnalyzerImpl;
import sourcemanager.SourceManager;
import sourcemanager.SourceManagerImpl;
import syntaxanalyzer.SyntaxAnalyzer;
import syntaxanalyzer.SyntaxAnalyzerImpl;
import utility.StringUtilities;

import java.io.FileNotFoundException;

public abstract class UserInterfaceImpl implements UserInterface{
    public void launch(String[] args) {
        SourceManager sourceManager = new SourceManagerImpl();
        ExceptionHandler exceptionHandler = new ExceptionHandler(sourceManager);

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl(sourceManager);
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerImpl(lexicalAnalyzer);

        long startingTimeInMillis = System.currentTimeMillis();

        String path = safelyGetPath(args, exceptionHandler);
        safelyOpenFile(path, sourceManager, exceptionHandler);

        try {
            syntaxAnalyzer.analyze();
        } catch(LexicalException ex) {
            exceptionHandler.handleLexicalException(ex);
        } catch(SyntaxException ex) {
            exceptionHandler.handleSyntaxException(ex);
        } catch(SemanticException ex) {
            exceptionHandler.handleSemanticException(ex);
        } catch(CompilerException ex) {
            exceptionHandler.handleGenericException(ex);
        }

        if(exceptionHandler.getExceptionsHandled() == 0) {
            StringUtilities.setTextToGreen();
            System.out.println("[SinErrores]");
            StringUtilities.setTextToWhite();
        }

        long finishingTimeInMillis = System.currentTimeMillis();
        long millisSpentCompiling = finishingTimeInMillis - startingTimeInMillis;

        System.out.println();
        System.out.println("--- Successfully compiled " + sourceManager.getLineNumber() +
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
