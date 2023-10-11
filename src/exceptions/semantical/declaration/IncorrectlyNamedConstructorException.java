package exceptions.semantical.declaration;

import exceptions.semantical.SemanticException;
import token.Token;

public class IncorrectlyNamedConstructorException extends SemanticException {
    protected Token classToken;
    public IncorrectlyNamedConstructorException(Token t, Token classToken) {
        super(t);
        this.classToken = classToken;
    }

    @Override
    protected String getSpecificMessage() {
        return "The constructor " + lexeme + " (declared in line " + lineNumber + ") isnt correctly named." +
                " It should have the same name as the class it's in (" + classToken.getLexeme() + ")";
    }
}
