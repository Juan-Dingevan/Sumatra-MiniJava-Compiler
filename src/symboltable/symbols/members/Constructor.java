package symboltable.symbols.members;

import symboltable.privacy.Privacy;
import symboltable.symbols.classes.ConcreteClass;
import token.Token;
import utility.StringUtilities;

public class Constructor extends Unit {
    private static final int LEVEL = 2;

    public static Constructor getDefaultConstructorForClass(ConcreteClass c) {
        Constructor constructor = new Constructor(c.getToken());
        constructor.setPrivacy(Privacy.pub);
        return constructor;
    }

    public Constructor(Token t) {
        super(t);
    }

    public String toString() {
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "CONSTRUCTOR PRIVACY:" + privacy + "\n";

        s += prefix + "PARAMETERS:\n";

        for(Parameter p : parameterMap.values())
            s += p.toString() + "\n";

        return s;
    }
}
