package exceptions.semantical.declaration;

import exceptions.semantical.SemanticException;
import token.Token;

public abstract class DeclarationException extends SemanticException {
    public DeclarationException(Token t) {
        super(t);
    }
}
