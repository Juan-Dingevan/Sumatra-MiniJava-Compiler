package exceptions.semantical.sentence;

import exceptions.semantical.SemanticException;
import symboltable.types.Type;
import token.Token;
import utility.Pair;

public class InvalidTypesInBinaryOperatorException extends SemanticException {
    protected Pair<Type, Type> expected;
    protected Pair<Type, Type> got;

    public InvalidTypesInBinaryOperatorException(Token t, Pair<Type, Type> expected, Pair<Type, Type> got) {
        super(t);
        this.expected = expected;
        this.got = got;
    }

    @Override
    protected String getSpecificMessage() {
        String e1 = expected.getFirstElement().toString();
        String e2 = expected.getSecondElement().toString();
        String g1 = got.getFirstElement().toString();
        String g2 = got.getSecondElement().toString();

        String s = "The operator " + lexeme + " was used with invalid types in line " + lineNumber + ". "
                 + "Expected (" + e1 + ", " + e2 + "), but got (" + g1 + ", " + g2 + ")";

        return s;
    }
}
