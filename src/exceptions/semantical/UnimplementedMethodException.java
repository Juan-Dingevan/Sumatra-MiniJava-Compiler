package exceptions.semantical;

import token.Token;

public class UnimplementedMethodException extends SemanticException{
    protected Token interfaceToken;
    public UnimplementedMethodException(Token t, Token it) {
        super(t);
        interfaceToken = it;
    }

    @Override
    protected String getSpecificMessage() {
        return "The method " + lexeme + " (declared in line " + lineNumber + ") of interface " + interfaceToken.getLexeme()
                + " isn't implemented.";
    }
}
