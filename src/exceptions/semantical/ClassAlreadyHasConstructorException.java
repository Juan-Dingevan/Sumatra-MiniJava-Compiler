package exceptions.semantical;

import token.Token;

public class ClassAlreadyHasConstructorException extends SemanticException{
    public ClassAlreadyHasConstructorException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "The constructor declared in line " + lineNumber + " is redundant. The class already has one.";
    }
}
