package symboltable.ast.sentencenodes;

import symboltable.ast.expressionnodes.ExpressionNode;

public class LocalVariableNode extends SentenceNode {
    public static int classID = 0;
    private final int id = classID;
    protected ExpressionNode expression;
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("var ");
        sb.append(token.getLexeme());
        sb.append("\n\t");
        sb.append("expression: \n\t");

        if(expression != null) {
            sb.append("\t\t");
            sb.append(expression);
        } else {
            sb.append("[null]");
        }

        sb.append("\n");

        return sb.toString();
    }
}
