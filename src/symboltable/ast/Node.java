package symboltable.ast;

import symboltable.symbols.classes.Class;
import symboltable.symbols.classes.ConcreteClass;
import token.Token;

public abstract class Node {
    public int stringDepth = 0;
    protected Token token;
    protected ConcreteClass contextClass;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public ConcreteClass getContextClass() {
        return contextClass;
    }

    public void setContextClass(ConcreteClass contextClass) {
        this.contextClass = contextClass;
    }

    protected String tabs() {
        String tab = "\t";
        return tab.repeat(stringDepth);
    }
}
