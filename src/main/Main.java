package main;

import exceptionhandler.ExceptionHandler;
import exceptions.*;
import lexicalanalizer.LexicalAnalyzer;
import lexicalanalizer.LexicalAnalyzerImpl;
import sourcemanager.SourceManager;
import sourcemanager.SourceManagerImpl;
import token.Token;
import utility.TokenType;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        boolean errores = false;
        SourceManager sourceManager = new SourceManagerImpl();
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl(sourceManager);
        ExceptionHandler exceptionHandler = new ExceptionHandler(sourceManager);

        try {
            String path = "./src/main/test.minijava";
            //validateCall(args);
            //String path = args[0];
            openFile(sourceManager, path);
        } catch(CompilerException ex) {
            exceptionHandler.handleGenericException(ex);
        }

        while(true) {
            try {
                Token t = lexicalAnalyzer.getNextToken();

                System.out.println(t);

                if (t.getTokenType() == TokenType.eof) {
                    break;
                }

            } catch(LexicalException ex) {
                errores = true;
                exceptionHandler.handleLexicalException(ex);
            }
            catch(CompilerException ex) {
                errores = true;
                exceptionHandler.handleGenericException(ex);
            }
        }

        if(!errores)
            System.out.println("[SinErrores]");

        /*for(int i = 1; i < 30; i++)
            System.out.println(
                    StringUtilities.removeAllConsecutiveWhitespacesAtTheStartOfString(sourceManager.getLine(i))
            );*/
    }

    private static void openFile(SourceManager sourceManager, String path) throws UnexpectedErrorException {
        try {
            sourceManager.open(path);
        } catch(FileNotFoundException ex) {
            throw new UnexpectedErrorException("finding the source file: No source file found");
        }
    }

    private static void validateCall(String[] args) throws CompilerCallException {
        if(args.length != 1)
            throw new CompilerCallException(args.length);
    }
}