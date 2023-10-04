package exceptions.semantical.declaration;

import token.Token;

public class ParameterAlreadyExistsException extends DeclarationException{
    public ParameterAlreadyExistsException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        String s = "The parameter " + lexeme + " declared in line " + lineNumber + " shares a name with another parameter of the same unit.";

        return s;
    }
}
