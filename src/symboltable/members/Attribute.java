package symboltable.members;

import symboltable.types.Type;
import utility.StringUtilities;

public class Attribute {
    private static int classID = 0;
    private static final int LEVEL = 2;

    protected int instanceID;
    protected Type type;
    protected String name;

    public Attribute(Type type, String name) {
        instanceID = classID;

        this.type = type;
        this.name = name;

        classID++;
    }

    public String toString() {
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "ATTRIBUTE{"+instanceID+"}: " + name + " TYPE: " + type;
        return s;
    }
}
