package exceptions.semantical.sentence;

import exceptions.semantical.SemanticException;
import token.Token;

public abstract class SentenceException extends SemanticException {
    public SentenceException(Token t) {
        super(t);
    }
}
