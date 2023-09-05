package exceptions.lexical;

public class InvalidCharacterException extends LexicalException{
    public InvalidCharacterException(int lineNumber, int lineIndexNumber, String lexeme, char currentCharAtMomentOfException) {
        super(lineNumber, lineIndexNumber, lexeme, currentCharAtMomentOfException);
    }

    public String getSpecificMessage() {
        return "Invalid character (" + currentCharAtMomentOfException + ") found in line: " + lineNumber + " at index: " + lineIndexNumber + ".";
    }
}
