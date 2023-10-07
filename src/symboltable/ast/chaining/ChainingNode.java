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
}
