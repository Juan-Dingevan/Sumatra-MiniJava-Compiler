package sourcemanager;

import utility.CharacterIdentifier;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SourceManagerImpl implements SourceManager{
    private BufferedReader reader;
    private String currentLine;
    private int lineNumber;
    private int lineIndexNumber;
    private boolean mustReadNextLine;

    public SourceManagerImpl() {
        currentLine = "";
        lineNumber = 0;
        lineIndexNumber = 0;
        mustReadNextLine = true;
    }

    @Override
    public void open(String filePath) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

        reader = new BufferedReader(inputStreamReader);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public char getNextChar() throws IOException {
        char currentChar = ' ';

        if(mustReadNextLine) {
            currentLine = reader.readLine();
            lineNumber++;
            lineIndexNumber = 0;
            mustReadNextLine = false;
        }

        if(lineIndexNumber < currentLine.length()) {
            currentChar = currentLine.charAt(lineIndexNumber);
            lineIndexNumber++;
        } else if (reader.ready()) {
            currentChar = '\n';
            mustReadNextLine = true;
        } else {
            currentChar = CharacterIdentifier.END_OF_FILE;
        }

        return currentChar;
    }

    @Override
    public int getLineNumber() {
        return lineNumber;
    }
    @Override
    public int getLineIndex() {
        return lineIndexNumber;
    }
    @Override
    public String getCurrentLine() {
        return currentLine;
    }
}
