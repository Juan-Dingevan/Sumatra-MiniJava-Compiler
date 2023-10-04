package exceptions.semantical.declaration;

import token.Token;

public class InterfaceAlreadyExistsException extends DeclarationException {
    public InterfaceAlreadyExistsException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        String s = "The interface " + lexeme + " declared in line " + lineNumber + " shares a name with a class or interface declared previously.";

        return s;
    }
}
