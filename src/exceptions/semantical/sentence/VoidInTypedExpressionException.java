package exceptions.semantical.sentence;

import token.Token;

public class VoidInTypedExpressionException extends SentenceException{
    public VoidInTypedExpressionException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "The expression in line " + lineNumber + " tries to use a void-typed entity.";
    }
}
