package symboltable.ast.sentencenodes;

import exceptions.general.CompilerException;
import symboltable.ast.Node;

public abstract class SentenceNode extends Node {
    public static final SentenceNode SEMICOLON_SENTENCE = null;
    protected BlockNode parentBlock;
    protected boolean hasBeenChecked;
    protected abstract int getID();

    public SentenceNode() {
        super();
        hasBeenChecked = false;
    }

    public BlockNode getParentBlock() {
        return parentBlock;
    }
    public void setParentBlock(BlockNode parentBlock) {
        this.parentBlock = parentBlock;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tabs());
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

    public final void check() throws CompilerException {
        if(!hasBeenChecked) {
            checkSelf();
            hasBeenChecked = true;
        }
    }

    protected void checkSelf() throws CompilerException {
        //TODO make abstract and implement it in each extension.
    }
}
