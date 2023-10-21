package symboltable.ast.expressionnodes.accesses;

import exceptions.general.CompilerException;
import symboltable.ast.chaining.ChainingNode;
import symboltable.ast.expressionnodes.AccessNode;
import symboltable.ast.expressionnodes.ExpressionNode;
import symboltable.types.Type;

public class ParenthesizedExpressionAccessNode extends AccessNode {
    ExpressionNode expression;

    public ExpressionNode getExpression() {
        return expression;
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    @Override
    public Type check() throws CompilerException {
        Type accessType = accessCheck();

        if(hasChaining()) {
            Type chainingType = chainingNode.check(accessType, expression.getToken());
            return chainingType;
        } else {
            return accessType;
        }
    }

    @Override
    protected Type accessCheck() throws CompilerException {
        return expression.check();
    }

    /*

    @Override
    protected boolean accessCanBeAssigned() {
        return expression.canBeAssigned();
    }

    */

    @Override
    public boolean isValidAsSentence() {
        return chainingNode != ChainingNode.NO_CHAINING;
    }

    @Override
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
