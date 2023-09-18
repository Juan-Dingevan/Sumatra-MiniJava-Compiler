package exceptions.semantical;

import token.Token;

public class InterfaceAlreadyExistsException extends SemanticException {
    public InterfaceAlreadyExistsException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        String s = "The interface " + lexeme + " declared in line " + lineNumber + " shares a name with a class or interface declared previously.";

        return s;
    }
}
