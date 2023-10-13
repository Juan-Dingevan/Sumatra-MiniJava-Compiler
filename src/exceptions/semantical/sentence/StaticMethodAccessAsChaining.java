package exceptions.semantical.sentence;

import exceptions.semantical.SemanticException;
import token.Token;

public class StaticMethodAccessAsChaining extends SemanticException {
    public StaticMethodAccessAsChaining(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "The method " + lexeme + " accessed in line " + lineNumber + " is static, and shouldn't be called";
    }
}
