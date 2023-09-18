package symboltable.symbols.classes;

import symboltable.symbols.members.Method;
import token.Token;
import utility.StringUtilities;

import java.util.HashMap;

public class Interface extends Class {
    private static int classID = 0;
    private static final int LEVEL = 1;

    protected int instanceID;
    protected HashMap<String, Method> methods;

    public Interface(Token t) {
        super(t);

        instanceID = classID;

        methods = new HashMap<>();

        classID++;
    }

    public String toString() {
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "INTERFACE{"+instanceID+"}: " + getName() + " EXTENDS: " + inheritsFrom + "\n";

        s+="METHOD HEADERS:\n";

        for(Method m : methods.values())
            s += m.toString() + "\n";

        return s;
    }
}
