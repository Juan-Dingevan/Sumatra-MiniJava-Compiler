package symboltable.ast.sentencenodes;

import symboltable.ast.Node;

public abstract class SentenceNode extends Node {
    protected BlockNode parentBlock;

    public BlockNode getParentBlock() {
        return parentBlock;
    }

    public void setParentBlock(BlockNode parentBlock) {
        this.parentBlock = parentBlock;
    }
}
