package exceptions.semantical.sentence;

import token.Token;

public class NoExpressionInTypedMethodReturnException extends SentenceException{
    protected Token unitToken;
    public NoExpressionInTypedMethodReturnException(Token t, Token unitToken) {
        super(t);
        this.unitToken = unitToken;
    }

    @Override
    protected String getSpecificMessage() {
        return "The return sentence of method " + unitToken.getLexeme() + ", in line " + lineNumber
                + " doesn't have an expression, but one was expected.";
    }
}
