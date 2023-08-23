package sourcemanager;

import utility.CharacterIdentifier;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class SourceManagerImpl implements SourceManager{
    BufferedReader reader;
    private int lineNumber;
    private int lineIndexNumber;
    private boolean mustUpdateLineAndIndexOnNextCharRead;

    public SourceManagerImpl() {
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
        }

        if(readInt == -1)
            readChar = CharacterIdentifier.END_OF_FILE;
        else
            readChar = (char) readInt;

        if(CharacterIdentifier.isEOL(readChar)) {
            mustUpdateLineAndIndexOnNextCharRead = true;
        } else {
            lineIndexNumber++;
        }

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
}
