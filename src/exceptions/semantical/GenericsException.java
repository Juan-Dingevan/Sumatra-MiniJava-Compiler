package exceptions.semantical;

import token.Token;

public class GenericsException extends SemanticException{
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
