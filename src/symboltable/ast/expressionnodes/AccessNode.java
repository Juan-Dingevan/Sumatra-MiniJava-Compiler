package symboltable.ast.expressionnodes;

import exceptions.general.CompilerException;
import symboltable.ast.chaining.ChainingNode;
import symboltable.types.Type;

public abstract class AccessNode extends OperandNode {
    protected ChainingNode chainingNode;

    public ChainingNode getChainingNode() {
        return chainingNode;
    }

    public void setChainingNode(ChainingNode chainingNode) {
        this.chainingNode = chainingNode;
    }

    public Type check() throws CompilerException {
        /**Type accessCheck = accessCheck();

        if(chainingNode != ChainingNode.NO_CHAINING) {
            chainingNode.check(...);
        } else {
            return accessCheck;
        }**/

        return null;
    }

    //protected abstract Type accessCheck() throws CompilerException;

    @Override
    public boolean isValidAsSentence() {
        if(chainingNode != ChainingNode.NO_CHAINING)
            return chainingNode.isCall();
        else
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

    public String getDeclarationForm() {
        String s = super.getDeclarationForm();
        String c = getChainingDeclarationForm();
        return s + c;
    }

    protected String getChainingDeclarationForm() {
        return chainingNode == ChainingNode.NO_CHAINING ? "" : chainingNode.getDeclarationForm();
    }
}
