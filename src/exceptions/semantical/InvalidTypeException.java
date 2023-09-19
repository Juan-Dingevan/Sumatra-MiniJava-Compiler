package exceptions.semantical;

import symboltable.types.Type;
import token.Token;

public class InvalidTypeException extends SemanticException{
    protected Type ty;
    public InvalidTypeException(Token t, Type ty) {
        super(t);
        this.ty = ty;
    }

    @Override
    protected String getSpecificMessage() {
        return "The type " + ty + " of " + lexeme + " (used in line " + lineNumber + ") is not valid in that context.";
    }
}
