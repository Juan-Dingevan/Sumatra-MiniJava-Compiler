package symboltable.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import exceptions.general.CompilerException;
import exceptions.semantical.ClassAlreadyExistsException;
import exceptions.semantical.NoMainMethodInProgramException;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.classes.Interface;
import symboltable.symbols.classes.Class;
import symboltable.symbols.members.Attribute;
import symboltable.symbols.members.Method;
import symboltable.symbols.members.Parameter;
import token.Token;

public class SymbolTable {
    private static SymbolTable instance;

    protected HashMap<String, ConcreteClass> classes;
    protected HashMap<String, Interface> interfaces;
    protected ConcreteClass currentConcreteClass;
    protected Interface currentInterface;
    protected Class currentClass;
    protected List<Method> mainMethods;
    protected Token eof;


    public static SymbolTable getInstance() {
        if(instance == null)
            instance = new SymbolTable();

        return instance;
    }

    public static void resetInstance() {
        instance = null;
        ConcreteClass.resetID();
        Attribute.resetID();
        Method.resetID();
        Parameter.resetID();
    }

    private SymbolTable() {
        classes = new HashMap<>();
        interfaces = new HashMap<>();
        mainMethods = new ArrayList<>();

        try {
            addClass(DefaultClassesSetUpHandler.getString());
            addClass(DefaultClassesSetUpHandler.getSystem());
            addClass(DefaultClassesSetUpHandler.getObject());
        } catch(CompilerException ex) {
            //this will never happen.
        }
    }
    public ConcreteClass getClass(String name) {
        return classes.get(name);
    }

    @SuppressWarnings("ReassignedVariable")
    public Class getClassOrInterface(String name) {
        Class ret = classes.get(name);
        if(ret == null)
            ret = interfaces.get(name);

        return ret;
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

    public boolean concreteClassExists(String name) {
        return getClass(name) != null;
    }

    public boolean interfaceExists(String name) {
        return getInterface(name) != null;
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

    public void checkDeclaration() throws CompilerException {

        for(Class c : classes.values()) {
            c.checkDeclaration();
        }

        for(Interface i : interfaces.values()) {
            i.checkDeclaration();
        }

        if(mainMethods.size() < 1)
            throw new NoMainMethodInProgramException(eof);
    }

    public void consolidate() throws CompilerException {

        for(Class c : classes.values()) {
            c.consolidate();
        }

        for(Interface i : interfaces.values()) {
            i.consolidate();
        }
    }

    public void addMainMethod(Method mainMethod) {
        mainMethods.add(mainMethod);
    }

    public void setEOF(Token eof) {
        this.eof = eof;
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
