package exceptions.semantical.sentence;

import exceptions.semantical.SemanticException;
import token.Token;

public class UndeclaredClassException extends SemanticException {
    public UndeclaredClassException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "The class " + lexeme + " used in line " + lineNumber + " is not declared.";
    }
}
