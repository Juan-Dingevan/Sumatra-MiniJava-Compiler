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
}
