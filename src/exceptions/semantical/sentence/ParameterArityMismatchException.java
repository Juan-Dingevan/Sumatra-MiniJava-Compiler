package exceptions.semantical.sentence;

import token.Token;

public class ParameterArityMismatchException extends SentenceException{
    protected int expected;
    protected int got;
    public ParameterArityMismatchException(Token t, int expected, int got) {
        super(t);
        this.expected = expected;
        this.got = got;
    }

    @Override
    protected String getSpecificMessage() {
        return "The unit " + lexeme + " called in line " + lineNumber + " does not have the correct amount of actual parameters." +
               "Expected " + expected + " but got " + got + ".";
    }
}
