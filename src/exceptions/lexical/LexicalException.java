package exceptions.lexical;

import exceptions.general.CompilationTimeException;

public abstract class LexicalException extends CompilationTimeException {
    protected int lineNumber;
    protected int lineIndexNumber;
    protected String lexeme;
    protected char currentCharAtMomentOfException;

    public LexicalException(int lineNumber, int lineIndexNumber, String lexeme, char currentCharAtMomentOfException) {
        super(lineNumber, lexeme);
        this.lineIndexNumber = lineIndexNumber;
        this.currentCharAtMomentOfException = currentCharAtMomentOfException;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getLineIndexNumber() {
        return lineIndexNumber;
    }

}
