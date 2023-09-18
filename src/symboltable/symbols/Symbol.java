package symboltable.symbols;

import token.Token;

public abstract class Symbol {
    protected Token token;
    public Symbol(Token t) {
        token = t;
    }

    public String getName() {
        return token.getLexeme();
    }

    public Token getToken() {
        return token;
    }
}
