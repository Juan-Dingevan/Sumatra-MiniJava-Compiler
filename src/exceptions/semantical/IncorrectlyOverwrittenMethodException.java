package exceptions.semantical;

import token.Token;

public class IncorrectlyOverwrittenMethodException extends SemanticException{
    protected Token classToken;
    public IncorrectlyOverwrittenMethodException(Token t, Token ct) {
        super(t);
        classToken = ct;
    }

    @Override
    protected String getSpecificMessage() {
        return "The method " + lexeme + " in class " + classToken.getLexeme()
                + " (declared in line " + lineNumber + ") is incorrectly overwritten.";
    }
}
