package exceptions.semantical.declaration;

import token.Token;

public class IncorrectlyOverwrittenMethodException extends DeclarationException{
    protected Token classToken;
    public IncorrectlyOverwrittenMethodException(Token t, Token ct) {
        super(t);
        classToken = ct;
    }

    @Override
    protected String getSpecificMessage() {
        return "The method " + lexeme + " in class " + classToken.getLexeme()
                + " (declared in line " + lineNumber + ") is incorrectly overwritten from ancestor."
                + " Overwritten methods should have the exact same signature (save for order of parameters).";
    }
}
