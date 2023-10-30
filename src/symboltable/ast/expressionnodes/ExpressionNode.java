package symboltable.ast.expressionnodes;

import exceptions.general.CompilerException;
import symboltable.ast.Node;
import symboltable.ast.sentencenodes.BlockNode;
import symboltable.types.Type;

public abstract class ExpressionNode extends Node {
    public static final ExpressionNode NULL_EXPRESSION = null;
    protected BlockNode parentBlock;

    public boolean canBeAssigned() {return false;}
    public boolean isAssignment() {
        return false;
    }
    public boolean isValidAsSentence() {
        return false;
    }

    public abstract Type check() throws CompilerException;

    public void setParentBlock(BlockNode p) {
        parentBlock = p;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tabs());
        sb.append(getClass().getSimpleName());
        sb.append("(");
        sb.append(token.getLexeme());
        sb.append(")");

        return sb.toString();
    }

}
