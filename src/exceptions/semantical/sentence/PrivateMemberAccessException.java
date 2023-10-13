package exceptions.semantical.sentence;

import exceptions.semantical.SemanticException;
import token.Token;

public class PrivateMemberAccessException extends SemanticException {
    public PrivateMemberAccessException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "The member " + lexeme + " accessed in line " + lineNumber + " is not visible in that context.";
    }
}
