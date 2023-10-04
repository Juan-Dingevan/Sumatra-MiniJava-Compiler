package exceptions.semantical.declaration;

import token.Token;

public class StaticMethodInInterfaceException extends DeclarationException{
    protected Token interfaceToken;
    public StaticMethodInInterfaceException(Token t, Token it) {
        super(t);
        interfaceToken = it;
    }

    @Override
    protected String getSpecificMessage() {
        return "The method " + lexeme + " (declared in line " + lineNumber + ") of interface " + interfaceToken.getLexeme() + " is static.";
    }
}
