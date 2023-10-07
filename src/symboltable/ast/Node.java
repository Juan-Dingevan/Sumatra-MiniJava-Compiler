package symboltable.ast;

import token.Token;

public abstract class Node {
    public int stringDepth = 0;
    protected Token token;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    protected String tabs() {
        String tab = "\t";
        return tab.repeat(stringDepth);
    }
}
