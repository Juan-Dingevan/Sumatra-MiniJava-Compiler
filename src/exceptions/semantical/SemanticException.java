package exceptions.semantical;

import exceptions.general.CompilationTimeException;
import token.Token;

public abstract class SemanticException extends CompilationTimeException {

    public SemanticException(Token t) {
        super(t.getLineNumber(), t.getLexeme());
    }
}
