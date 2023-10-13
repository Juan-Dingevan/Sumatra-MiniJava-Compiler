package exceptions.semantical.sentence;

import exceptions.semantical.SemanticException;
import symboltable.types.Type;
import token.Token;

public class InvalidTypesInUnaryOperatorException extends SemanticException {
    protected Type expected;
    protected Type got;

    public InvalidTypesInUnaryOperatorException(Token t, Type expected, Type got) {
        super(t);
        this.expected = expected;
        this.got = got;
    }

    @Override
    protected String getSpecificMessage() {
        String s = "The operator " + lexeme + " was used with invalid types in line " + lineNumber + ". "
                + "Expected " + expected.toString() + ", but got " + got.toString() + ".";

        return s;
    }
}
