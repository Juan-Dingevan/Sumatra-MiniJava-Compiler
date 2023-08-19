package exceptions;

public class IncorrectlyFormedCharLiteralException extends LexicalException{
    public IncorrectlyFormedCharLiteralException(int lineNumber, int lineIndexNumber, String lexeme, char currentCharAtMomentOfException) {
        super(lineNumber, lineIndexNumber, lexeme, currentCharAtMomentOfException);
    }
}
