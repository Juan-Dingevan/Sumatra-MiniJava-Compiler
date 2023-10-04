package exceptions.semantical.declaration;

import token.Token;

public class GenericsException extends DeclarationException {
    private final String message;
    public GenericsException(Token t, String message) {
        super(t);
        this.message = message;
    }

    @Override
    protected String getSpecificMessage() {
        return message;
    }
}
