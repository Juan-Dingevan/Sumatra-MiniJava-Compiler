package symboltable.symbols.members;

import symboltable.types.Type;
import token.Token;
import utility.StringUtilities;

public class Attribute extends TypedEntity {
    private static int classID = 0;
    private static final int LEVEL = 2;

    protected int instanceID;
    protected boolean isStatic;

    public Attribute(Token t) {
        super(t);

        instanceID = classID;
        classID++;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public String toString() {
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "ATTRIBUTE{"+instanceID+"}: " + getName() + " TYPE: " + type
                 + " STATIC: " + isStatic + " PRIVACY: " + privacy + "\n";

        return s;
    }
}
