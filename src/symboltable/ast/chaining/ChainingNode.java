package symboltable.ast.chaining;

import exceptions.general.CompilerException;
import symboltable.ast.Node;
import symboltable.types.Type;
import token.Token;

public abstract class ChainingNode extends Node {
    public static final ChainingNode NO_CHAINING = null;
    protected ChainingNode chainingNode;

    public ChainingNode getChainingNode() {
        return chainingNode;
    }

    public void setChainingNode(ChainingNode chainingNode) {
        this.chainingNode = chainingNode;
    }

    public boolean hasChaining() {
        return chainingNode != NO_CHAINING;
    }

    public Type check(Type callerType, Token callerToken) throws CompilerException {
        Type selfType = checkSelf(callerType, callerToken);

        if(hasChaining())
            return chainingNode.check(selfType, token);
        else
            return selfType;
    }

    protected abstract Type checkSelf(Type callerType, Token callerToken) throws CompilerException;

    public abstract String getDeclarationForm();

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
