package symboltable.ast.sentencenodes;

import symboltable.ast.expressionnodes.ExpressionNode;

public class AssignmentNode extends SentenceNode{
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
}
