package exceptionhandler;

import exceptions.general.CompilerException;
import exceptions.lexical.LexicalException;
import exceptions.semantical.SemanticException;
import exceptions.syntax.SyntaxException;
import sourcemanager.SourceManager;
import utility.Pair;
import utility.StringUtilities;

public class ExceptionHandler {
    private static final String DETAIL_PREFIX = "Detail: ";
    private final SourceManager sourceManager;
    private int exceptionsHandled;
    public ExceptionHandler(SourceManager sourceManager) {
        this.sourceManager = sourceManager;
        exceptionsHandled = 0;
    }
    private void updateCounter() {
        exceptionsHandled++;
    }
    public void handleGenericException(CompilerException ex) {
        updateCounter();

        System.out.println();
        StringUtilities.setTextToRed();

        System.out.println("ERROR: " + ex.getMessage());

        StringUtilities.setTextToWhite();
        System.out.println();
    }

    public void handleLexicalException(LexicalException ex) {
        updateCounter();

        System.out.println();
        StringUtilities.setTextToRed();

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

        StringUtilities.setTextToWhite();
        System.out.println();
    }

    public void handleSyntaxException(SyntaxException ex) {
        updateCounter();

        System.out.println();
        StringUtilities.setTextToRed();

        System.out.println(ex.getMessage());

        StringUtilities.setTextToWhite();
        System.out.println();
    }

    public void handleSemanticException(SemanticException ex) {
        updateCounter();

        System.out.println();
        StringUtilities.setTextToRed();

        System.out.println(ex.getMessage());

        StringUtilities.setTextToWhite();
        System.out.println();
    }

    public int getExceptionsHandled() {
        return exceptionsHandled;
    }
}
