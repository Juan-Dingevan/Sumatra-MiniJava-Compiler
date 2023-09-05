package exceptions.lexical;

public class IncorrectlyFormedStringLiteralException extends LexicalException{
    public IncorrectlyFormedStringLiteralException(int lineNumber, int lineIndexNumber, String lexeme, char currentCharAtMomentOfException) {
        super(lineNumber, lineIndexNumber, lexeme, currentCharAtMomentOfException);
    }

    protected String getSpecificMessage() {
        return "Error while building a string literal, in line " + lineNumber + " at index " + lineIndexNumber + ".";
    }
}
