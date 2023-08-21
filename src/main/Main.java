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

        try {
            validateCall(args);
            //String path = "./src/main/test.minijava";
            String path = args[0];
            openFile(sourceManager, path);
        } catch(CompilerException ex) {
            ExceptionHandler.handle(ex);
        }

        while(true) {
            try {
                Token t = lexicalAnalyzer.getNextToken();

                System.out.println(t);

                if (t.getTokenType() == TokenType.eof) {
                    break;
                }

            } catch(CompilerException ex) {
                errores = true;
                ExceptionHandler.handle(ex);
            }
        }

        if(!errores)
            System.out.println("[SinErrores]");
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