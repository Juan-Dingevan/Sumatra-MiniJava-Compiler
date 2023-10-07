package symboltable.ast.expressionnodes.accesses;

import symboltable.ast.expressionnodes.AccessNode;
import token.Token;

public class ConstructorAccessNode extends AccessNode {
    protected Token classToken;

    public Token getClassToken() {
        return classToken;
    }

    public void setClassToken(Token classToken) {
        this.classToken = classToken;
    }
}
