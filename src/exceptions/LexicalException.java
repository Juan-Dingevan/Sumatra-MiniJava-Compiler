package exceptions;

public class LexicalException extends CompilerException{
    protected int lineNumber;
    protected int lineIndexNumber;
    protected String lexeme;
    protected char currentCharAtMomentOfException;
    public LexicalException(int lineNumber, int lineIndexNumber, String lexeme, char currentCharAtMomentOfException) {
        this.lineNumber = lineNumber;
        this.lineIndexNumber = lineIndexNumber;
        this.lexeme = lexeme;
        this.currentCharAtMomentOfException = currentCharAtMomentOfException;
    }

    public String getMessage() {
        return getSpecificMessage() + "\n" + getErrorCode();
    }

    protected String getErrorCode() {
        return "[Error:" + lexeme + "|" + lineNumber + "]";
    }

    protected String getSpecificMessage() {
        return "";
    }


}
