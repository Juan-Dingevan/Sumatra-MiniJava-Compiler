package symboltable.symbols.members;

import symboltable.types.Type;
import token.Token;
import utility.StringUtilities;

public class Parameter extends TypedEntity{
    private static int classID = 0;
    private static final int LEVEL = 3;
    protected int instanceID;

    public Parameter(Token t) {
        super(t);
        instanceID = classID;
        classID++;
    }

    public String toString() {
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "PARAMETER{"+instanceID+"}: " + getName() + " TYPE: " + type;
        return s;
    }
}
