package exceptions.semantical.declaration;

import token.Token;

public class ClassAlreadyExistsException extends DeclarationException {
    public ClassAlreadyExistsException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        String s = "The class " + lexeme + " declared in line " + lineNumber + " shares a name with a class or interface declared previously.";

        return s;
    }
}
