package symboltable.ast.expressionnodes;

import exceptions.general.CompilerException;
import symboltable.ast.Node;
import symboltable.types.Type;

public abstract class ExpressionNode extends Node {
    public static final ExpressionNode NULL_EXPRESSION = null;

    public boolean isAssignment() {
        return false;
    }
    public boolean isValidAsSentence() {
        return false;
    }

    public abstract Type check() throws CompilerException;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tabs());
        sb.append(getClass().getSimpleName());
        sb.append("(");
        sb.append(token.getLexeme());
        sb.append(")");

        return sb.toString();
    }

    public String getDeclarationForm() {
        return token.getLexeme();
    }
}
