package exceptions.semantical;

import token.Token;

public class ParameterAlreadyExistsException extends SemanticException{
    public ParameterAlreadyExistsException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        String s = "The parameter " + lexeme + " declared in line " + lineNumber + " shares a name with another parameter of the same unit.";

        return s;
    }
}
