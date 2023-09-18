package symboltable.symbols.members;

import symboltable.types.Type;
import utility.StringUtilities;

public class Parameter {
    private static int classID = 0;
    private static final int LEVEL = 3;

    protected int instanceID;
    protected Type type;
    protected String name;

    public Parameter(Type type, String name) {
        instanceID = classID;

        this.type = type;
        this.name = name;

        classID++;
    }

    public String toString() {
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "PARAMETER{"+instanceID+"}: " + name + " TYPE: " + type;
        return s;
    }
}
