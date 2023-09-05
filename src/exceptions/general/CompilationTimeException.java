package exceptions.general;

public abstract class CompilationTimeException extends CompilerException {
    protected int lineNumber;
    protected String lexeme;

    public CompilationTimeException(int lineNumber, String lexeme) {
        this.lineNumber = lineNumber;
        this.lexeme = lexeme;
    }

    protected String getErrorCode() {
        return "[Error:"+lexeme+"|"+lineNumber+"]";
    }
    protected abstract String getSpecificMessage();

    public String getMessage() {
        return getSpecificMessage() + "\n" + getErrorCode();
    }
}
