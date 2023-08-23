package exceptionhandler;

import exceptions.CompilerException;
import exceptions.LexicalException;
import sourcemanager.SourceManager;
import utility.Pair;
import utility.StringUtilities;

public class ExceptionHandler {
    private static final String DETAIL_PREFIX = "Detail: ";
    private SourceManager sourceManager;
    public ExceptionHandler(SourceManager sourceManager) {
        this.sourceManager = sourceManager;
    }
    public void handleGenericException(CompilerException ex) {
        System.out.println();
        setTextToRed();

        System.out.println("ERROR: " + ex.getMessage());

        setTextToWhite();
        System.out.println();
    }

    public void handleLexicalException(LexicalException ex) {
        System.out.println();
        setTextToRed();

        int lineNumber = ex.getLineNumber();
        int lineIndex = ex.getLineIndexNumber();
        String line = sourceManager.getCurrentLine();

        Pair<Integer, String> cleanStringAndNumberOfBlanks = StringUtilities.removeAllConsecutiveWhitespacesAtTheStartOfString(line);
        String cleanLine = cleanStringAndNumberOfBlanks.getSecondElement();

        int numberOfBlanks = cleanStringAndNumberOfBlanks.getFirstElement();
        int detailIndex = DETAIL_PREFIX.length() + lineIndex - numberOfBlanks - 1;

        String detail = DETAIL_PREFIX + cleanLine;
        String pointer = StringUtilities.repeatCharacter(' ', detailIndex) + "^";

        System.out.println("ERROR: " + ex.getMessage());
        System.out.println(detail);
        System.out.println(pointer);

        setTextToWhite();
        System.out.println();
    }

    private void setTextToWhite() {
        System.out.print("\u001B[0m");
    }

    protected void setTextToRed() {
        System.out.print("\u001B[31m");
    }
}
