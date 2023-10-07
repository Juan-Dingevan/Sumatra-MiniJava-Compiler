package symboltable.ast.expressionnodes;

public abstract class UnaryExpressionNode extends ExpressionNode{
    protected ExpressionNode operandExpression;
    public ExpressionNode getOperandExpression() {
        return operandExpression;
    }

    public void setOperandExpression(ExpressionNode operandExpression) {
        this.operandExpression = operandExpression;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("\n\t");
        sb.append("Operand: \n\t\t");
        sb.append(operandExpression);

        return sb.toString();
    }
}
