package userinterface;

import codegenerator.CodeGenerator;
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
import symboltable.table.SymbolTable;
import syntaxanalyzer.SyntaxAnalyzer;
import syntaxanalyzer.SyntaxAnalyzerImpl;
import utility.StringUtilities;

import java.io.FileNotFoundException;

public abstract class UserInterfaceImpl implements UserInterface {
    private static final boolean DEBUG = false;
    public void launch(String[] args) {
        String outputName;

        if(args.length == 2) {
            outputName = args[1];
        } else {
            outputName = CodeGenerator.DEFAULT_OUTPUT_NAME;
        }

        SymbolTable.resetInstance();

        SourceManager sourceManager = new SourceManagerImpl();
        ExceptionHandler exceptionHandler = new ExceptionHandler(sourceManager);

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl(sourceManager);
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerImpl(lexicalAnalyzer);

        long startingTimeInMillis = System.currentTimeMillis();

        String path = safelyGetPath(args, exceptionHandler);
        safelyOpenFile(path, sourceManager, exceptionHandler);

        try {
            syntaxAnalyzer.analyze();

            SymbolTable.getInstance().checkDeclaration();
            SymbolTable.getInstance().consolidate();

            SymbolTable.getInstance().checkSentences();

            CodeGenerator.getInstance().open(outputName);

            SymbolTable.getInstance().generate();

            CodeGenerator.getInstance().close();
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

        if(DEBUG) {
            System.out.println();
            System.out.println(SymbolTable.getInstance());
        }

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
