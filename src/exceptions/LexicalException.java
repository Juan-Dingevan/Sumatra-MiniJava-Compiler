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


}
