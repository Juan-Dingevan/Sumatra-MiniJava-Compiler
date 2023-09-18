package symboltable.table;

import java.util.HashMap;

import exceptions.general.CompilerException;
import exceptions.semantical.ClassAlreadyExistsException;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.classes.Interface;
import symboltable.symbols.classes.Class;

public class SymbolTable {
    private static SymbolTable instance;

    protected HashMap<String, ConcreteClass> classes;
    protected HashMap<String, Interface> interfaces;
    protected ConcreteClass currentConcreteClass;
    protected Interface currentInterface;
    protected Class currentClass;


    public static SymbolTable getInstance() {
        if(instance == null)
            instance = new SymbolTable();

        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    private SymbolTable() {
        classes = new HashMap<>();
        interfaces = new HashMap<>();
        currentConcreteClass = null;
        currentInterface = null;
    }
    public ConcreteClass getClass(String name) {
        return classes.get(name);
    }

    public ConcreteClass getCurrentConcreteClass() {
        return currentConcreteClass;
    }

    public Class getCurrentClassOrInterface() {
        return currentClass;
    }

    public Interface getInterface(String name) {
        return interfaces.get(name);
    }

    public Interface getCurrentInterface() {
        return currentInterface;
    }

    public boolean exists(String name) {
        return getClass(name) != null || getInterface(name) != null;
    }

    public void addClass(ConcreteClass c) throws CompilerException {
        if(!exists(c.getName())) {
            classes.put(c.getName(), c);
            currentConcreteClass = c;
            currentClass = c;
        } else {
            throw new ClassAlreadyExistsException(c.getToken());
        }
    }

    public void addInterface(Interface i) throws CompilerException {
        if(!exists(i.getName())) {
            interfaces.put(i.getName(), i);
            currentInterface = i;
            currentClass = i;
        } else {
            throw new ClassAlreadyExistsException(i.getToken());
        }
    }

    public String toString() {
        String s = "SYMBOL TABLE:\n";

        s += "CLASSES:\n";

        for(ConcreteClass c : classes.values())
            s += c.toString() + "\n";

        s += "INTERFACES:\n";

        for(Interface i : interfaces.values())
            s += i.toString() + "\n";

        return s;
    }
}
