package symboltable.symbols.classes;

import symboltable.symbols.Symbol;
import token.Token;

public abstract class Class extends Symbol {
    protected String inheritsFrom;

    public Class(Token t) {
        super(t);
        inheritsFrom = "";
    }

    public void setInheritance(Token t) {
        inheritsFrom = t.getLexeme();
    }
}
