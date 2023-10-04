package exceptions.semantical.declaration;

import token.Token;

public class AttributeAlreadyExistsException extends DeclarationException{
    public AttributeAlreadyExistsException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        String s = "The attribute " + lexeme + " declared in line " + lineNumber + " shares a name with another attribute of the same class.";

        return s;
    }
}
