package symboltable.ast.sentencenodes;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import symboltable.ast.expressionnodes.ExpressionNode;
import symboltable.types.Type;

public class CallNode extends SentenceNode{
    public static int classID = 0;
    private final int id = classID;
    protected ExpressionNode expression;
    protected Type type;
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

    @Override
    protected void checkSelf() throws CompilerException {
        type = expression.check();
    }

    @Override
    public void generate() throws CompilerException {
        expression.generate();

        if(!Type.isVoid(type)) {
            String cPop = " # We made a call to a return-giving method, but we won't use the result, so we pop it.";
            CodeGenerator.getInstance().append("POP" + cPop);
        }
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
