package symboltable.ast.expressionnodes.accesses;

import symboltable.ast.expressionnodes.AccessNode;
import symboltable.ast.expressionnodes.ExpressionNode;

public class ParenthesizedExpressionAccessNode extends AccessNode {
    ExpressionNode expression;

    public ExpressionNode getExpression() {
        return expression;
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("\n");
        sb.append(tabs());
        sb.append("expression: \n");
        expression.stringDepth = stringDepth + 1;
        sb.append(expression);

        return sb.toString();
    }
}
