package exceptions.semantical.sentence;

import token.Token;

public class LocalVariableInitializedWithNullException extends SentenceException {
    public LocalVariableInitializedWithNullException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "Error in line " + lineNumber + " the variable " + lexeme + " was initialized with 'null'.";
    }
}
