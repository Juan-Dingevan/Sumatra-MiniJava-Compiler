package symboltable.members;

import utility.StringUtilities;

import java.util.HashMap;

public class Interface {
    private static int classID = 0;
    private static final int LEVEL = 1;

    protected int instanceID;
    protected HashMap<String, Method> methods;
    protected String name;
    protected String inheritsFrom;

    public Interface(String name) {
        instanceID = classID;

        methods = new HashMap<>();

        inheritsFrom = "";

        this.name = name;

        classID++;
    }

    public String toString() {
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "INTERFACE{"+instanceID+"}:" + name + " EXTENDS: " + instanceID;

        s+="METHOD HEADERS:\n";

        for(Method m : methods.values())
            s += m.toString() + "\n";

        return s;
    }
}
