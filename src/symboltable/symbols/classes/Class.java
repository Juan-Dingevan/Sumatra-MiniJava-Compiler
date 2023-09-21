package symboltable.symbols.classes;

import exceptions.general.CompilerException;
import exceptions.semantical.MethodAlreadyExistsException;
import symboltable.symbols.Symbol;
import symboltable.symbols.members.Method;
import token.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Class extends Symbol {
    protected HashMap<String, Method> methods;
    protected List<String> genericTypes;
    protected List<String> parentDeclaredGenericTypes;
    protected String inheritsFrom;
    protected Method currentMethod;

    public Class(Token t) {
        super(t);

        methods = new HashMap<>();
        genericTypes = new ArrayList<>();
        parentDeclaredGenericTypes = new ArrayList<>();
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
    public Iterable<Method> getMethods() {
        return methods.values();
    }

    public void addMethod(Method m) throws CompilerException {
        if(!methodExists(m)) {
            methods.put(m.getName(), m);
            currentMethod = m;
        } else
            throw new MethodAlreadyExistsException(m.getToken());
    }

    public List<String> getGenericTypes() {
        return genericTypes;
    }
    public void setGenericTypes(List<String> genericTypes) {
        this.genericTypes = genericTypes;
    }
    public boolean isGenericType(String genericType) {
        return genericTypes.contains(genericType);
    }

    public List<String> getParentDeclaredGenericTypes() {
        return parentDeclaredGenericTypes;
    }

    public void setParentDeclaredGenericTypes(List<String> parentDeclaredGenericTypes) {
        this.parentDeclaredGenericTypes = parentDeclaredGenericTypes;
    }

    public abstract void consolidate() throws CompilerException;
}
