package exceptions.semantical.sentence;

import exceptions.semantical.SemanticException;
import token.Token;

public class UnresolvedNameException extends SemanticException {
    protected Token classToken;

    public UnresolvedNameException(Token t, Token classToken) {
        super(t);
        this.classToken = classToken;
    }

    @Override
    protected String getSpecificMessage() {
        return "The name " + lexeme + " couldn't be resolved in the context of " + classToken.getLexeme() + ".";
    }
}
