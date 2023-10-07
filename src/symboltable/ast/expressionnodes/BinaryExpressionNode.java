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
}
