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
        sb.append("\n");
        sb.append(tabs());
        sb.append("Operand: \n");
        operandExpression.stringDepth = stringDepth+1;
        sb.append(operandExpression);

        return sb.toString();
    }

    public String getDeclarationForm() {
        String operand = operandExpression.getDeclarationForm();
        return token.getLexeme() + operand;
    }
}
