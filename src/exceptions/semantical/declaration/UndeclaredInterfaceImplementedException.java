package exceptions.semantical.declaration;

import token.Token;

public class UndeclaredInterfaceImplementedException extends DeclarationException{
    protected String interfaceName;
    public UndeclaredInterfaceImplementedException(Token t, String interfaceName) {
        super(t);
        this.interfaceName = interfaceName;
    }

    @Override
    protected String getSpecificMessage() {
        return "The interface " + interfaceName + " implemented by " + lexeme + " (in line " + lineNumber + ") is not declared.";
    }

    public String getErrorCode() {
        return "[Error:" + interfaceName + "|" + lineNumber + "]";
    }
}
