package exceptions.semantical.declaration;

import symboltable.table.SymbolTable;
import token.Token;

public class UndeclaredExtendsException extends DeclarationException{
    protected String className;
    public UndeclaredExtendsException(Token t, String className) {
        super(t);
        this.className = className;
    }

    @Override
    protected String getSpecificMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("The class ");
        sb.append(className);
        sb.append(" extended by ");
        sb.append(lexeme);
        sb.append(" (in line ");
        sb.append(lineNumber);
        sb.append(") is not declared.");

        if(SymbolTable.getInstance().interfaceExists(className)) {
            sb.append("\n");
            sb.append("(");
            sb.append(className);
            sb.append(" is declared as an interface, which can't be extended by classes).");
        } else if(SymbolTable.getInstance().concreteClassExists(className)) {
            sb.append("\n");
            sb.append("(");
            sb.append(className);
            sb.append(" is declared as a concrete class, which can't be extended by interfaces).");
        }

        return sb.toString();
    }

    public String getErrorCode() {
        return "[Error:" + className + "|" + lineNumber + "]";
    }
}
