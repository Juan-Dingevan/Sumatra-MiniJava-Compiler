package symboltable.ast.sentencenodes;

import exceptions.general.CompilerException;
import symboltable.ast.expressionnodes.ExpressionNode;
import symboltable.symbols.members.Variable;
import symboltable.types.Type;

public class LocalVariableNode extends SentenceNode {
    public static int classID = 0;
    private final int id = classID;
    protected ExpressionNode expression;
    protected Variable variable;
    @Override
    protected int getID() {
        return id;
    }
    public ExpressionNode getExpression() {
        return expression;
    }
    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    @Override
    protected void checkSelf() throws CompilerException {
        Type expressionType = expression.check();
        variable.setType(expressionType);
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("var ");
        sb.append(token.getLexeme());
        sb.append("\n");
        sb.append(tabs());
        sb.append("expression");

        if(expression != null) {
            if(variable != null) {
                sb.append(" (type ");
                sb.append(variable.getType());
                sb.append(")");
            }

            sb.append(": \n");
            expression.stringDepth = stringDepth+1;
            sb.append(expression);
        } else {
            sb.append(": \n");
            sb.append(tabs());
            sb.append("[null]");
        }

        sb.append("\n");

        return sb.toString();
    }
}
