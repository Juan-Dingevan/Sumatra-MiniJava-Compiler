package exceptions.semantical.declaration;

import token.Token;

public class UnimplementedMethodException extends DeclarationException{
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
