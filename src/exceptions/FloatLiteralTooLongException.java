package exceptions;

public class FloatLiteralTooLongException extends LexicalException {
    public FloatLiteralTooLongException(int lineNumber, int lineIndexNumber, String lexeme, char currentCharAtMomentOfException) {
        super(lineNumber, lineIndexNumber, lexeme, currentCharAtMomentOfException);
    }

    public String getSpecificMessage() {
        return "The float " + lexeme + " in line " + lineNumber + " at index " + lineIndexNumber + " is too long.\n"
                + "The float is " + lexeme.length() + " and the maximum is 16";
    }
}
