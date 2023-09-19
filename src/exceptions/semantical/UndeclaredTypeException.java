package exceptions.semantical;

import symboltable.types.Type;
import token.Token;

public class UndeclaredTypeException extends SemanticException{
    protected Type type;
    public UndeclaredTypeException(Token t, Type type) {
        super(t);
        this.type = type;
    }

    @Override
    protected String getSpecificMessage() {
        return "The type " + type + " of " + lexeme + " (used in line " + lineNumber + ") is not declared.";
    }
}
