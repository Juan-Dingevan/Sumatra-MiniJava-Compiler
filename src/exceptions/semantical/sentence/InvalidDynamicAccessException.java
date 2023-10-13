package exceptions.semantical.sentence;

import exceptions.semantical.SemanticException;
import token.Token;

public class InvalidDynamicAccessException extends SemanticException {
    protected Token classToken;
    public InvalidDynamicAccessException(Token t, Token classToken) {
        super(t);
        this.classToken = classToken;
    }

    @Override
    protected String getSpecificMessage() {
        String s = "The method " + lexeme + " is dynamic, and can't be accessed through a reference to the class " + classToken.getLexeme()
                 + " (done in line " + lineNumber + "). It must be accessed through a variable of type " + classToken.getLexeme() + ".";

        return s;
    }
}
