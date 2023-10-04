package exceptions.semantical.declaration;

import token.Token;

public class MethodAlreadyExistsException extends DeclarationException {
    public MethodAlreadyExistsException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        String s = "The method " + lexeme + " declared in line " + lineNumber + " shares a name with another method of the same class or interface.";

        return s;
    }
}
