package codegenerator;

import exceptions.general.CompilerException;
import exceptions.general.UnexpectedErrorException;
import symboltable.ast.sentencenodes.SentenceNode;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.members.Constructor;
import symboltable.symbols.members.Member;
import symboltable.symbols.members.Method;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeGenerator {
    public static final String DEFAULT_OUTPUT_NAME = "a.out"; //we love u, gcc
    private static CodeGenerator instance;
    private static int lastTagID = 0;
    private String fileName;
    private FileWriter fileWriter;

    public static String getMethodTag(Method m) {
        String methodName = m.getName();
        String className = m.getMemberOf().getName();
        String tag = methodName + "@" + className;
        return tag;
    }

    public static String getConstructorTag(Constructor c) {
        String className = c.getMemberOf().getName();
        String tag = "Constructor@" + className;
        return tag;
    }

    public static String getVTableTag(ConcreteClass c) {
        return "VTable@" + c.getName();
    }

    public static String getSentenceTag(SentenceNode s) {
        String lexeme  = s.getToken().getLexeme();
        String unit    = s.getContextUnit().getName();
        String context = s.getContextClass().getName();

        String tag = lexeme + "@" + unit + "@" + context + lastTagID;

        lastTagID++;

        return tag;
    }

    public static CodeGenerator getInstance() {
        if(instance == null)
            instance = new CodeGenerator();

        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    private CodeGenerator() {}

    public void open(String fileName) throws CompilerException {
        try {
            this.fileName = fileName;
            fileWriter = new FileWriter(fileName);
        } catch(IOException ex) {
            throw new UnexpectedErrorException("IO Error while opening output file.");
        }
    }

    public void close() throws CompilerException {
        try {
            fileWriter.close();
        } catch(IOException ex) {
            throw new UnexpectedErrorException("IO Error while closing output file.");
        }
    }

    public void append(String lineWithoutReturn) throws CompilerException {
        String line = lineWithoutReturn + "\n";
        try {
            fileWriter.write(line);
        } catch (IOException ex) {
            throw new UnexpectedErrorException("IO Error while writing to output file.");
        }
    }

    public void addBreakLine() throws CompilerException {
        append("");
    }

}
