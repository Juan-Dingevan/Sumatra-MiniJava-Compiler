package exceptions.semantical.sentence;

import token.Token;

public class ExpressionInVoidOrConstructorReturn extends SentenceException{
    protected Token unitToken;
    public ExpressionInVoidOrConstructorReturn(Token t, Token unitToken) {
        super(t);
        this.unitToken = unitToken;
    }

    @Override
    protected String getSpecificMessage() {
        return "The unit " + unitToken.getLexeme() + " has an expression in its return sentence," +
                "even though its return type is void or it's a constructor. In line " + lineNumber;
    }
}
