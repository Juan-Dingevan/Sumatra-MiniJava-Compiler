package exceptions;

public class InvalidCharacterException extends LexicalException{
    public InvalidCharacterException(int lineNumber, int lineIndexNumber, String lexeme, char currentCharAtMomentOfException) {
        super(lineNumber, lineIndexNumber, lexeme, currentCharAtMomentOfException);
    }

    public String getMessage() {
        return "Invalid character (" + (int) currentCharAtMomentOfException + ") found in line: " + lineNumber + " at index: " + lineIndexNumber + ".";
    }
}
