package exceptions.semantical.declaration;

import token.Token;

public class ClassAlreadyHasConstructorException extends DeclarationException{
    public ClassAlreadyHasConstructorException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "The constructor declared in line " + lineNumber + " is redundant. The class already has one.";
    }
}
