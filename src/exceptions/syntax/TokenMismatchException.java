package exceptions.syntax;

import token.Token;
import token.TokenType;

public class TokenMismatchException extends SyntaxException{
    private TokenType typeExpected;
    private Token tokenRead;
    public TokenMismatchException(TokenType typeExpected, Token tokenRead) {
        super(tokenRead.getLineNumber(), tokenRead.getLexeme());
        this.typeExpected = typeExpected;
        this.tokenRead = tokenRead;
    }

    public String getSpecificMessage() {
        return "Error in line " + lineNumber + ". Expected " + typeExpected + " but got " + tokenRead.getLexeme()
                + " (" + tokenRead.getTokenType() + ").";
    }
}
