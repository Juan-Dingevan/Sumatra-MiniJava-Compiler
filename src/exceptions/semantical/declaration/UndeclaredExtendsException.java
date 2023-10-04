package exceptions.semantical.declaration;

import token.Token;

public class UndeclaredExtendsException extends DeclarationException{
    protected String className;
    public UndeclaredExtendsException(Token t, String className) {
        super(t);
        this.className = className;
    }

    @Override
    protected String getSpecificMessage() {
        return "The class " + className + " extended by " + lexeme + " (in line " + lineNumber + ") is not declared.";
    }

    public String getErrorCode() {
        return "[Error:" + className + "|" + lineNumber + "]";
    }
}
