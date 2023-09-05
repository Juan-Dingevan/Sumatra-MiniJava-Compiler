package exceptions.lexical;

public class FloatLiteralOutOfRange extends LexicalException {
    public FloatLiteralOutOfRange(int lineNumber, int lineIndexNumber, String lexeme, char currentCharAtMomentOfException) {
        super(lineNumber, lineIndexNumber, lexeme, currentCharAtMomentOfException);
    }

    public String getSpecificMessage() {
        return "The float " + lexeme + " in line " + lineNumber + " at index " + lineIndexNumber + " is not in the valid range of values specified by norm IEEE754.";
    }
}
