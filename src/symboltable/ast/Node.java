package symboltable.ast;

import exceptions.general.CompilerException;
import symboltable.symbols.classes.Class;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.members.Unit;
import token.Token;

public abstract class Node {
    public int stringDepth = 0;
    protected Token token;
    protected ConcreteClass contextClass;
    protected Unit contextUnit;

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

    public Unit getContextUnit() {
        return contextUnit;
    }

    public void setContextUnit(Unit contextUnit) {
        this.contextUnit = contextUnit;
    }

    public void generate() throws CompilerException {
        //TODO: make abstract
    }
    protected String tabs() {
        String tab = "\t";
        return tab.repeat(stringDepth);
    }
}
