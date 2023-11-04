package symboltable.ast.chaining;

import exceptions.general.CompilerException;
import exceptions.semantical.declaration.GenericsException;
import symboltable.ast.Node;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import token.Token;

public abstract class ChainingNode extends Node {
    public static final ChainingNode NO_CHAINING = null;
    protected ChainingNode chainingNode;
    protected boolean isAssignmentLHS;
    public ChainingNode() {
        isAssignmentLHS = false;
    }

    public ChainingNode getChainingNode() {
        return chainingNode;
    }

    public void setChainingNode(ChainingNode chainingNode) {
        this.chainingNode = chainingNode;
    }

    public boolean hasChaining() {
        return chainingNode != NO_CHAINING;
    }

    public void setAssignmentLHS(boolean assignmentLHS) {
        isAssignmentLHS = assignmentLHS;

        if(hasChaining())
            chainingNode.setAssignmentLHS(assignmentLHS);
    }

    public Type check(Type callerType, Token callerToken) throws CompilerException {
        Type selfType = checkSelf(callerType, callerToken);

        checkDiamondNotation(callerType, callerToken);

        if(hasChaining())
            return chainingNode.check(selfType, token);
        else
            return selfType;
    }

    private static void checkDiamondNotation(Type callerType, Token callerToken) throws GenericsException {
        if(Type.isReferenceType(callerType)) {
            ReferenceType rt = (ReferenceType) callerType;
            if(rt.usesDiamondNotation()) {
                String error = "Error in line " + callerToken.getLineNumber() + " generic types can't be inferred in that context.";
                throw new GenericsException(callerToken, error);
            }
        }
    }

    public boolean canBeAssigned() {
        if(hasChaining())
            return chainingNode.canBeAssigned();
        else
            return selfCanBeAssigned();
    }

    protected abstract boolean selfCanBeAssigned();

    protected abstract Type checkSelf(Type callerType, Token callerToken) throws CompilerException;
    public abstract boolean isCall();

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
}
