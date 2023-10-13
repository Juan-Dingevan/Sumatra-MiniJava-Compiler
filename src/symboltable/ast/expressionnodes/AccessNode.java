package symboltable.ast.expressionnodes;

import exceptions.general.CompilerException;
import symboltable.ast.chaining.ChainingNode;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.types.Type;

public abstract class AccessNode extends OperandNode {
    protected ChainingNode chainingNode;
    protected ConcreteClass contextClass;

    public ChainingNode getChainingNode() {
        return chainingNode;
    }

    public void setChainingNode(ChainingNode chainingNode) {
        this.chainingNode = chainingNode;
    }

    @Override
    public ConcreteClass getContextClass() {
        return contextClass;
    }

    @Override
    public void setContextClass(ConcreteClass contextClass) {
        this.contextClass = contextClass;
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

    protected abstract Type accessCheck() throws CompilerException;

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
