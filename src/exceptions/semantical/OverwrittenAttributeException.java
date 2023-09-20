package exceptions.semantical;

import token.Token;

public class OverwrittenAttributeException extends SemanticException{
    Token originalAttributeToken;
    public OverwrittenAttributeException(Token t, Token oat) {
        super(t);
        originalAttributeToken = oat;
    }

    @Override
    protected String getSpecificMessage() {
        return "The attribute " + lexeme + " (declared in line " + lineNumber + ") was already declared in "
                + "a parent class (line " + originalAttributeToken.getLineNumber() + ").";
    }
}
