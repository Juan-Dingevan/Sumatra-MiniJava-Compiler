package exceptions.semantical.sentence;

import exceptions.semantical.SemanticException;
import token.Token;

public class PrimitiveTypeHasChainingException extends SemanticException {
    public PrimitiveTypeHasChainingException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "Primitive types can't have chaining.";
    }
}
