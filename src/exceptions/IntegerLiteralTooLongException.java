package exceptions;

public class IntegerLiteralTooLongException extends LexicalException{
    public IntegerLiteralTooLongException(int lineNumber, int lineIndexNumber, String lexeme, char currentCharAtMomentOfException) {
        super(lineNumber, lineIndexNumber, lexeme, currentCharAtMomentOfException);
    }

    public String getMessage() {
        String message = "The integer " + lexeme + " in line " + lineNumber + " column " + lineIndexNumber + " is too long.\n"
                       + "The integer is " + lexeme.length() + " and the maximum is 9";

        return message;
    }
}
