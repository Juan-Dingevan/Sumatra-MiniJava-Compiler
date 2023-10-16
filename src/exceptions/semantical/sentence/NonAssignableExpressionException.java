package exceptions.semantical.sentence;

import token.Token;

public class NonAssignableExpressionException extends SentenceException{
    public NonAssignableExpressionException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "The assignment in line " + lineNumber + " is not valid because the left hand side is not assignable.";
    }
}
