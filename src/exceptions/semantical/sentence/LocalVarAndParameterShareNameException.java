package exceptions.semantical.sentence;

import token.Token;

public class LocalVarAndParameterShareNameException extends SentenceException{
    public LocalVarAndParameterShareNameException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "The local variable " + lexeme + " declared in line " + lineNumber + " shares a name with a parameter.";
    }
}
