package symboltable.ast.chaining;

import symboltable.ast.Node;

public abstract class ChainingNode extends Node {
    public static final ChainingNode NO_CHAINING = null;
    protected ChainingNode chainingNode;

    public ChainingNode getChainingNode() {
        return chainingNode;
    }

    public void setChainingNode(ChainingNode chainingNode) {
        this.chainingNode = chainingNode;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tabs());
        sb.append(getClass().getSimpleName());
        sb.append("(");
        sb.append(token.getLexeme());
        sb.append(")");

        if(chainingNode != NO_CHAINING) {
            sb.append("\n");
            sb.append(tabs());
            sb.append("chaining: \n");
            chainingNode.stringDepth = stringDepth + 1;
            sb.append(chainingNode);
        }

        return sb.toString();
    }
    public abstract String getDeclarationForm();
}
