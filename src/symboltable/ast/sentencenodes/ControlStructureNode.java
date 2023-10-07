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
        sb.append("sentence:\n");

        if(sentence != null) {
            sentence.stringDepth = stringDepth + 1;
            sb.append(sentence);
            sb.append("\n");
        } else {
            sb.append("null\n");
        }

        return sb.toString();
    }
}
