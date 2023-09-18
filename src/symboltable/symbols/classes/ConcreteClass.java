package symboltable.symbols.classes;

import symboltable.symbols.members.Attribute;
import symboltable.symbols.members.Constructor;
import symboltable.symbols.members.Method;
import token.Token;
import utility.StringUtilities;

import java.util.HashMap;

public class ConcreteClass extends Class {
    private static int classID = 0;
    private static final int LEVEL = 1;

    protected int instanceID;
    protected HashMap<String, Attribute> attributes;
    protected HashMap<String, Method> methods;
    protected Constructor constructor;
    protected Method currentMethod;
    protected Token token;
    protected String implementsInterface;

    public ConcreteClass(Token t) {
        super(t);

        instanceID = classID;

        attributes = new HashMap<>();
        methods = new HashMap<>();

        currentMethod = null;
        constructor = null;

        implementsInterface = "";

        classID++;
    }

    public Method getCurrentMethod() {
        return currentMethod;
    }


    public void setImplements(Token t) {
        implementsInterface = t.getLexeme();
    }

    public String toString() {
        String name = this.getName();
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "CLASS{" + instanceID + "}: " + name + " EXTENDS: " + inheritsFrom + " IMPLEMENTS: " + implementsInterface + "\n";

        s += prefix + "ATTRIBUTES:\n";

        for(Attribute a : attributes.values())
            s += a.toString() + "\n";

        s += prefix + "METHODS:\n";

        for(Method m : methods.values())
            s += m.toString() + "\n";

        s += prefix + "CONSTRUCTOR: " + (constructor == null ? "" : constructor.toString()) + "\n";

        return s;
    }
}
