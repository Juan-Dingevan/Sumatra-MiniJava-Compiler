package symboltable.ast;

import token.Token;

public abstract class Node {
    protected Token token;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
