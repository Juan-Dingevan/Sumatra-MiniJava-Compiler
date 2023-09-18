package symboltable.symbols.members;

import symboltable.types.Type;
import utility.StringUtilities;

import java.util.HashMap;

public class Method {
    private static int classID = 0;
    private static final int LEVEL = 2;

    protected int instanceID;
    protected Type returnType;
    protected String name;
    protected HashMap<String, Parameter> parameters;


    public Method(Type returnType, String name) {
        instanceID = classID;

        parameters = new HashMap<>();

        this.returnType = returnType;
        this.name = name;

        classID++;
    }

    public String toString() {
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "METHOD{" + instanceID + "}: " + name + " RETURN TYPE: " + returnType;

        s += prefix + "PARAMETERS:\n";

        for(Parameter p : parameters.values())
            s += p.toString() + "\n";

        return s;
    }
}
