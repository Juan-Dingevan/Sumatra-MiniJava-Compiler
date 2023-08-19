package exceptions;

public class IncorrectlyFormedStringLiteralException extends LexicalException{
    public IncorrectlyFormedStringLiteralException(int lineNumber, int lineIndexNumber, String lexeme, char currentCharAtMomentOfException) {
        super(lineNumber, lineIndexNumber, lexeme, currentCharAtMomentOfException);
    }
}
