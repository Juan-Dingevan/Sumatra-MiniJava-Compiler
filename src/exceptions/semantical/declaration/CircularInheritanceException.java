package exceptions.semantical.declaration;

import token.Token;

public class CircularInheritanceException extends DeclarationException {
    public CircularInheritanceException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "The class " + lexeme + " (declared in line" + lineNumber + ") has circular inheritance.";
    }
}
