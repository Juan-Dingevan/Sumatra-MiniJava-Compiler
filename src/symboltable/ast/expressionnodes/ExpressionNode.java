package symboltable.ast.expressionnodes;

import symboltable.ast.Node;

public abstract class ExpressionNode extends Node {
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("(");
        sb.append(token.getLexeme());
        sb.append(")");

        return sb.toString();
    }
}
