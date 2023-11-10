package symboltable.ast.sentencenodes;

import exceptions.general.CompilerException;
import symboltable.ast.expressionnodes.ExpressionNode;

public class AssignmentNode extends SentenceNode{
    public static int classID = 0;
    private final int id;

    protected ExpressionNode expression;

    public AssignmentNode() {
        super();
        id = classID;
        classID++;
    }
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

    public void checkSelf() throws CompilerException {
        expression.check();
    }

    @Override
    public void generate() throws CompilerException {
        expression.generate();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());

        sb.append("\n");
        sb.append(tabs());
        sb.append("expression: \n");

        if(expression != null) {
            expression.stringDepth = stringDepth+1;
            sb.append(expression);
        } else {
            sb.append(tabs());
            sb.append("[null]");
        }

        return sb.toString();
    }
}
