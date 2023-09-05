package exceptions.lexical;

public class IncorrectlyFormedOrOperatorException extends LexicalException{
    public IncorrectlyFormedOrOperatorException(int lineNumber, int lineIndexNumber, String lexeme, char currentCharAtMomentOfException) {
        super(lineNumber, lineIndexNumber, lexeme, currentCharAtMomentOfException);
    }

    protected String getSpecificMessage() {
        return "Error, expected | but found " + currentCharAtMomentOfException + " (in line " + lineNumber + " at index " + lineIndexNumber + ").";
    }
}
