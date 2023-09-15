package symboltable.table;

import java.util.HashMap;
import symboltable.members.Class;
import symboltable.members.Interface;
import symboltable.members.Method;

public class SymbolTable {
    private static SymbolTable instance;

    protected HashMap<String, Class> classes;
    protected HashMap<String, Interface> interfaces;
    protected Class currentClass;
    protected Method currentMethod;


    public static SymbolTable getInstance() {
        if(instance == null)
            instance = new SymbolTable();

        return instance;
    }

    private SymbolTable() {
        classes = new HashMap<>();
        interfaces = new HashMap<>();
        currentClass = null;
        currentMethod = null;
    }
    public Class getClass(String name) {
        return classes.get(name);
    }

    public Interface getInterface(String name) {
        return interfaces.get(name);
    }

    public void addClass(Class c) {
        classes.put(c.getName(), c);
        currentClass = c;
    }
}
