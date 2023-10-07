package exceptions.semantical.sentence;

import token.Token;

public class LocalVariableAlreadyExistsException extends SentenceException {
    public LocalVariableAlreadyExistsException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "The variable " + lexeme + " declared in line " + lineNumber + " shares a name with an existing variable in scope.";
    }
}
