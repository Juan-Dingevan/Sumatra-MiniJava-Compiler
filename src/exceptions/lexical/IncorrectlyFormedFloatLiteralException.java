package exceptions.lexical;

public class IncorrectlyFormedFloatLiteralException extends LexicalException{
    public IncorrectlyFormedFloatLiteralException(int lineNumber, int lineIndexNumber, String lexeme, char currentCharAtMomentOfException) {
        super(lineNumber, lineIndexNumber, lexeme, currentCharAtMomentOfException);
    }

    protected String getSpecificMessage() {
        return "Error while building a float literal, in line " + lineNumber + " at index " + lineIndexNumber + ".\n" +
                "Expected a numerical digit but got: " + currentCharAtMomentOfException;
    }
}
