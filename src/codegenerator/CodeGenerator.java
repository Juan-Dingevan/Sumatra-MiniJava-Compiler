package codegenerator;

import exceptions.general.CompilerException;
import exceptions.general.UnexpectedErrorException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeGenerator {
    public static final String DEFAULT_OUTPUT_NAME = "a.out"; //we love u, gcc
    private static CodeGenerator instance;
    private String fileName;
    private FileWriter fileWriter;

    private CodeGenerator() {}

    public static CodeGenerator getInstance() {
        if(instance == null)
            instance = new CodeGenerator();

        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    public void open(String fileName) throws IOException {
        this.fileName = fileName;
        fileWriter = new FileWriter(fileName);
    }

    public void close() throws IOException {
        fileWriter.close();
    }

    public void append(String lineWithoutReturn) throws CompilerException {
        String line = lineWithoutReturn + "\n";
        try {
            fileWriter.write(line);
        } catch (IOException ex) {
            throw new UnexpectedErrorException("IO Error while writing to output file.");
        }
    }

}
