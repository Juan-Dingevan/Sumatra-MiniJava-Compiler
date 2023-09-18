package symboltable.symbols.members;

import token.Token;
import utility.StringUtilities;

public class Constructor extends Unit {
    private static final int LEVEL = 2;
    public Constructor(Token t) {
        super(t);
    }

    public String toString() {
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "CONSTRUCTOR PRIVACY:" + privacy + "\n";

        s += prefix + "PARAMETERS:\n";

        for(Parameter p : parameters.values())
            s += p.toString() + "\n";

        return s;
    }
}
