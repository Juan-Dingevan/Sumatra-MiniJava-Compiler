package symboltable.symbols.classes;

import exceptions.general.CompilerException;
import exceptions.semantical.MethodAlreadyExistsException;
import symboltable.symbols.Symbol;
import symboltable.symbols.members.Method;
import token.Token;

import java.util.HashMap;

public abstract class Class extends Symbol {
    protected HashMap<String, Method> methods;
    protected String inheritsFrom;
    protected Method currentMethod;

    public Class(Token t) {
        super(t);

        methods = new HashMap<>();
        currentMethod = null;

        inheritsFrom = "";
    }

    public void setInheritance(Token t) {
        inheritsFrom = t.getLexeme();
    }

    public Method getCurrentMethod() {
        return currentMethod;
    }

    protected boolean methodExists(Method m) {
        return methods.get(m.getName()) != null;
    }

    protected Method getMethod(String name) {
        return methods.get(name);
    }

    public void addMethod(Method m) throws CompilerException {
        if(!methodExists(m)) {
            methods.put(m.getName(), m);
            currentMethod = m;
        } else
            throw new MethodAlreadyExistsException(m.getToken());
    }

    public abstract void consolidate() throws CompilerException;
}
