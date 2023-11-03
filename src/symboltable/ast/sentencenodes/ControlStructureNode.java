package symboltable.ast.sentencenodes;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.TypesDontConformException;
import symboltable.ast.expressionnodes.ExpressionNode;
import symboltable.types.SBoolean;
import symboltable.types.Type;

public abstract class ControlStructureNode extends SentenceNode{
    protected ExpressionNode expression;
    protected BlockNode implicitBlock;

    public ControlStructureNode() {
        implicitBlock = new BlockNode();
    }

    @Override
    public void setParentBlock(BlockNode parentBlock) {
        super.setParentBlock(parentBlock);
        implicitBlock.setParentBlock(parentBlock);
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    public BlockNode getImplicitBlock() {
        return implicitBlock;
    }

    public void setSentence(SentenceNode sentence) {
        implicitBlock.addSentence(sentence);
    }

    public void giveLocalVariablesOffset() {
        implicitBlock.giveLocalVariablesOffset();
    }

    @Override
    protected void checkSelf() throws CompilerException {
        Type expressionType = expression.check();

        if(!expressionType.equals(new SBoolean()))
            throw new TypesDontConformException(token, new SBoolean(), expressionType);

        implicitBlock.check();
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

        sb.append("\n");
        sb.append(tabs());
        sb.append("implicitBlock:\n");

        if(implicitBlock != null) {
            implicitBlock.stringDepth = stringDepth + 1;
            sb.append(implicitBlock);
            sb.append("\n");
        } else {
            sb.append("null\n");
        }

        return sb.toString();
    }
}
