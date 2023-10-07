package symboltable.ast.expressionnodes;

import symboltable.ast.Node;

public abstract class ExpressionNode extends Node {
    public static final ExpressionNode NULL_EXPRESSION = null;
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

    public boolean isAssignment() {
        return false;
    }
    public boolean isValidAsSentence() {
        return false;
    }
}
