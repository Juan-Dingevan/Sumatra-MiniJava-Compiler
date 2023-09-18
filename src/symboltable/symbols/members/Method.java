package symboltable.symbols.members;

import symboltable.types.Type;
import token.Token;
import utility.StringUtilities;

import java.util.HashMap;

public class Method extends Unit{
    private static int classID = 0;
    private static final int LEVEL = 2;

    protected int instanceID;
    protected Type returnType;

    public Method(Token t) {
        super(t);

        instanceID = classID;
        classID++;
    }

    public void setReturnType(Type type) {
        this.returnType = type;
    }

    public Type getReturnType() {
        return returnType;
    }

    public String toString() {
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "METHOD{" + instanceID + "}: " + getName() + " RETURN TYPE: " + returnType;

        s += prefix + "PARAMETERS:\n";

        for(Parameter p : parameters.values())
            s += p.toString() + "\n";

        return s;
    }
}
