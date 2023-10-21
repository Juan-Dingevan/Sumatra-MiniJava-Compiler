package exceptions.semantical.sentence;

import token.Token;

public class ThisAccessInStaticMethodException extends SentenceException{
    public ThisAccessInStaticMethodException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "Error in line " + lineNumber + " 'this' accesses are forbidden in static methods.";
    }
}
