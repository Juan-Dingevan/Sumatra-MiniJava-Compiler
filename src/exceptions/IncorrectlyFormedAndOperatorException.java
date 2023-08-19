package exceptions;

public class IncorrectlyFormedAndOperatorException extends LexicalException{
    public IncorrectlyFormedAndOperatorException(int lineNumber, int lineIndexNumber, String lexeme, char currentCharAtMomentOfException) {
        super(lineNumber, lineIndexNumber, lexeme, currentCharAtMomentOfException);
    }

    protected String getSpecificMessage() {
        return "Error, expected & but found " + currentCharAtMomentOfException + " (in line " + lineNumber + "at index " + lineIndexNumber + ").";
    }
}
