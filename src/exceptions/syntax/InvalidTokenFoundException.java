package exceptions.syntax;

import token.TokenType;

public class InvalidTokenFoundException extends SyntaxException{
    private final TokenType[] validTokens;
    public InvalidTokenFoundException(int lineNumber, String lexeme, TokenType[] validTokens) {
        super(lineNumber, lexeme);
        this.validTokens = validTokens;
    }

    @Override
    protected String getSpecificMessage() {
        String errorMessage = "Error in line " + lineNumber + ". Found " + lexeme + " but expected one of the following tokens: ";
        String tokenList = "";

        for(TokenType tt : validTokens)
            tokenList += tt + ", ";

        tokenList = tokenList.substring(0, tokenList.length()-1);

        return errorMessage + tokenList + ".";
    }
}
