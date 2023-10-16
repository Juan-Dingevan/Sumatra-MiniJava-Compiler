package symboltable.ast.expressionnodes;

public abstract class BinaryExpressionNode extends ExpressionNode {
    protected ExpressionNode lhs;
    protected ExpressionNode rhs;

    public ExpressionNode getLHS() {
        return lhs;
    }

    public void setLHS(ExpressionNode lhs) {
        this.lhs = lhs;
    }

    public ExpressionNode getRHS() {
        return rhs;
    }

    public void setRHS(ExpressionNode rhs) {
        this.rhs = rhs;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());

        sb.append("\n");
        sb.append(tabs());
        sb.append("Left Hand Side: \n");
        lhs.stringDepth = stringDepth + 1;
        sb.append(lhs);

        sb.append("\n");
        sb.append(tabs());
        sb.append("Right Hand Side: \n");
        rhs.stringDepth = stringDepth + 1;
        sb.append(rhs);

        return sb.toString();
    }

}
