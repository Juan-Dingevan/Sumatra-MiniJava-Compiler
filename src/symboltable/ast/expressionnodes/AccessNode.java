package symboltable.ast.expressionnodes;

import exceptions.general.CompilerException;
import symboltable.ast.chaining.ChainingNode;
import symboltable.ast.sentencenodes.BlockNode;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.members.Unit;
import symboltable.types.Type;

public abstract class AccessNode extends OperandNode {
    protected ChainingNode chainingNode;
    public ChainingNode getChainingNode() {
        return chainingNode;
    }
    protected boolean isAssignmentLHS;

    public AccessNode() {
        isAssignmentLHS = false;
    }

    public void setChainingNode(ChainingNode chainingNode) {
        this.chainingNode = chainingNode;
    }

    public boolean hasChaining() {
        return chainingNode != ChainingNode.NO_CHAINING;
    }

    public Type check() throws CompilerException {
        Type accessType = accessCheck();

        if(hasChaining()) {
            Type chainingType = chainingNode.check(accessType, token);
            return chainingType;
        } else {
            return accessType;
        }
    }

    public void setAssignmentLHS(boolean assignmentLHS) {
        isAssignmentLHS = assignmentLHS;

        if(hasChaining())
            chainingNode.setAssignmentLHS(assignmentLHS);
    }

    protected abstract Type accessCheck() throws CompilerException;

    @Override
    public final void generate() throws CompilerException {
        accessGenerate();

        if(hasChaining())
            chainingNode.generate();
    }

    protected abstract void accessGenerate() throws CompilerException;

    @Override
    public boolean isValidAsSentence() {
        if(chainingNode != ChainingNode.NO_CHAINING)
            return chainingNode.isCall();
        else
            return false;
    }

    @Override
    public boolean canBeAssigned() {
        if(hasChaining())
            return chainingNode.canBeAssigned();
        else
            return accessCanBeAssigned();
    }

    protected boolean accessCanBeAssigned() {
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());

        if(chainingNode != ChainingNode.NO_CHAINING) {
            sb.append("\n");
            sb.append(tabs());
            sb.append("chaining: \n");
            chainingNode.stringDepth = stringDepth + 1;
            sb.append(chainingNode);
        }

        return sb.toString();
    }
}
