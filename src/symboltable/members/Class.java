package symboltable.members;

import utility.StringUtilities;

import java.util.HashMap;

public class Class {
    private static int classID = 0;
    private static final int LEVEL = 1;

    protected int instanceID;
    protected HashMap<String, Attribute> attributes;
    protected HashMap<String, Method> methods;
    protected HashMap<String, Constructor> constructors;
    protected String inheritsFrom;
    protected String implementsInterface;
    protected String name;

    public Class(String name) {
        instanceID = classID;

        attributes = new HashMap<>();
        methods = new HashMap<>();
        constructors = new HashMap<>();

        inheritsFrom = "";
        implementsInterface = "";

        this.name = name;

        classID++;
    }

    public String toString() {
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "CLASS{" + instanceID + "}: " + name + " EXTENDS: " + inheritsFrom + " IMPLEMENTS: " + implementsInterface + "\n";

        s += prefix + "ATTRIBUTES:\n";

        for(Attribute a : attributes.values())
            s += a.toString() + "\n";

        s += prefix + "METHODS:\n";

        for(Method m : methods.values())
            s += m.toString() + "\n";

        s += prefix + "CONSTRUCTORS:\n";

        for(Constructor c : constructors.values())
            s += c.toString() + "\n";

        return s;
    }

    public String getName() {
        return name;
    }
}
