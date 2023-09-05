package exceptions.syntax;

import exceptions.general.CompilationTimeException;

public abstract class SyntaxException extends CompilationTimeException {
    public SyntaxException(int lineNumber, String lexeme) {
        super(lineNumber, lexeme);
    }
}
