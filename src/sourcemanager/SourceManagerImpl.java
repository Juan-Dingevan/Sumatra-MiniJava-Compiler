package sourcemanager;

import utility.CharacterIdentifier;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SourceManagerImpl implements SourceManager{
    private BufferedReader reader;
    private List<String> linesRead;
    private String currentLine;
    private int lineNumber;
    private int lineIndexNumber;
    private boolean mustUpdateLineAndIndexOnNextCharRead;

    public SourceManagerImpl() {
        linesRead = new ArrayList<>();
        currentLine = "";
        linesRead.add(currentLine);

        lineNumber = 1;
        lineIndexNumber = 0;

        mustUpdateLineAndIndexOnNextCharRead = false;
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
        int readInt = reader.read();
        char readChar;

        if(mustUpdateLineAndIndexOnNextCharRead) {
            lineNumber++;
            lineIndexNumber = 0;
            mustUpdateLineAndIndexOnNextCharRead = false;
            currentLine = "";
        }

        if(readInt == -1)
            readChar = CharacterIdentifier.END_OF_FILE;
        else
            readChar = (char) readInt;

        if(CharacterIdentifier.isEOL(readChar)) {
            mustUpdateLineAndIndexOnNextCharRead = true;
        } else {
            currentLine += readChar;

            if(linesRead.size() > 0)
                linesRead.remove( linesRead.size() - 1);

            lineIndexNumber++;
        }

        linesRead.add(currentLine);

        return readChar;
    }

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public int getLineIndex() {
        return lineIndexNumber;
    }
    public String getLine(int lineNumber) {
        int index = lineNumber - 1;
        return linesRead.get(index);
    }
}
