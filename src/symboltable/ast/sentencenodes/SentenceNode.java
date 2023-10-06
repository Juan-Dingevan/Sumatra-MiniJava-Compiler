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

    protected abstract int getID();

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("{");
        sb.append(getID());
        sb.append("} ");

        if(parentBlock != BlockNode.NULL_PARENT) {
            sb.append("parent BlockNode{");
            sb.append(parentBlock == null ? "null" : parentBlock.getID());
            sb.append("} ");
        }

        return sb.toString();
    }
}
