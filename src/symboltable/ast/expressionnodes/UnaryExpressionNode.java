package symboltable.ast.expressionnodes;

public abstract class UnaryExpressionNode extends ExpressionNode{
    protected ExpressionNode operandExpression;
    public ExpressionNode getOperandExpression() {
        return operandExpression;
    }

    public void setOperandExpression(ExpressionNode operandExpression) {
        this.operandExpression = operandExpression;
    }
}
