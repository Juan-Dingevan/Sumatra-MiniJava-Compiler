package sourcemanager;

import utility.CharacterIdentifier;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SourceManagerImpl implements SourceManager{
    BufferedReader reader;
    private int lineNumber;
    private int lineIndexNumber;

    public SourceManagerImpl() {
        lineNumber = 1;
        lineIndexNumber = 0;
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

        if(readInt == -1)
            readChar = CharacterIdentifier.END_OF_FILE;
        else
            readChar = (char) readInt;

        if(CharacterIdentifier.isEOL(readChar)) {
            lineNumber++;
            lineIndexNumber = 0;
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
