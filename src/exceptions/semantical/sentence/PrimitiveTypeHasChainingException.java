package exceptions.semantical.sentence;

import exceptions.semantical.SemanticException;
import symboltable.types.Type;
import token.Token;

public class PrimitiveTypeHasChainingException extends SemanticException {
    protected Type type;
    public PrimitiveTypeHasChainingException(Token t, Type type) {
        super(t);
        this.type = type;
    }

    @Override
    protected String getSpecificMessage() {
        return "The type " + type + " can't have chaining.";
    }
}
