package symboltable.ast.expressionnodes;

import symboltable.ast.chaining.ChainingNode;

public abstract class AccessNode extends OperandNode {
    protected ChainingNode chainingNode;

    public ChainingNode getChainingNode() {
        return chainingNode;
    }

    public void setChainingNode(ChainingNode chainingNode) {
        this.chainingNode = chainingNode;
    }
}
