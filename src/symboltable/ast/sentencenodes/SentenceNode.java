package symboltable.ast.sentencenodes;

import exceptions.general.CompilerException;
import symboltable.ast.Node;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.members.Unit;

public abstract class SentenceNode extends Node {
    public static final SentenceNode SEMICOLON_SENTENCE = null;
    protected BlockNode parentBlock;
    protected boolean hasBeenChecked;
    protected abstract int getID();

    public static boolean isBlock(SentenceNode s) {
        return s instanceof BlockNode;
    }

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

    public void giveLocalVariablesOffset() {}

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
        } else {
            sb.append("parent NULL_PARENT\n");
        }

        return sb.toString();
    }

    public final void check() throws CompilerException {
        if(!hasBeenChecked) {
            checkSelf();
            hasBeenChecked = true;
        }
    }

    protected abstract void checkSelf() throws CompilerException;
}
