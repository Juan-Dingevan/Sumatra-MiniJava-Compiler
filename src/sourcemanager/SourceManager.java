package sourcemanager;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface SourceManager {
    void open(String filePath) throws FileNotFoundException;
    void close() throws IOException;
    char getNextChar() throws IOException;
    int getLineNumber();
    int getLineIndex();
    String getCurrentLine();

}
