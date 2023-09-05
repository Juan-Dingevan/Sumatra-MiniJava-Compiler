package exceptions.lexical;

public class MultiLineCommentLeftEmptyException extends LexicalException{
    public MultiLineCommentLeftEmptyException(int lineNumber, int lineIndexNumber, String lexeme, char currentCharAtMomentOfException) {
        super(lineNumber, lineIndexNumber, lexeme, currentCharAtMomentOfException);
    }

    public String getSpecificMessage() {
        return "A multi-line comment was left empty at line: " + lineNumber + " column: " + lineIndexNumber + ".";
    }
}
