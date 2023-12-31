package exceptions.lexical;

public class IntegerLiteralTooLongException extends LexicalException{
    public IntegerLiteralTooLongException(int lineNumber, int lineIndexNumber, String lexeme, char currentCharAtMomentOfException) {
        super(lineNumber, lineIndexNumber, lexeme, currentCharAtMomentOfException);
    }

    public String getSpecificMessage() {
        return "The integer " + lexeme + " in line " + lineNumber + " at index " + lineIndexNumber + " is too long.\n"
                + "The integer is " + lexeme.length() + " and the maximum is 9";
    }
}
