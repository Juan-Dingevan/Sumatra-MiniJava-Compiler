package exceptions;

public class IncorrectlyFormedOrOperatorException extends LexicalException{
    public IncorrectlyFormedOrOperatorException(int lineNumber, int lineIndexNumber, String lexeme, char currentCharAtMomentOfException) {
        super(lineNumber, lineIndexNumber, lexeme, currentCharAtMomentOfException);
    }
}
