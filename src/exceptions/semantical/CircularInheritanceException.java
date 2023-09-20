package exceptions.semantical;

import token.Token;

public class CircularInheritanceException extends SemanticException{
    public CircularInheritanceException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "The class " + lexeme + " (declared in line" + lineNumber + ") has circular inheritance.";
    }
}
