package symboltable.ast.sentencenodes;

import symboltable.ast.expressionnodes.ExpressionNode;

public abstract class ControlStructureNode extends SentenceNode{
    protected ExpressionNode expression;
    protected SentenceNode sentence;

    public ExpressionNode getExpression() {
        return expression;
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    public SentenceNode getSentence() {
        return sentence;
    }

    public void setSentence(SentenceNode sentence) {
        this.sentence = sentence;
    }
}
