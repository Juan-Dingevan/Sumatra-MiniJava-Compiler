package symboltable.symbols.members;

import exceptions.general.CompilerException;
import symboltable.privacy.Privacy;
import symboltable.symbols.classes.Class;
import token.Token;

public class Variable extends TypedEntity {
    public static boolean isAttribute(Variable v) {
        return v instanceof Attribute;
    }

    public static int classID = 0;
    protected int instanceID;
    public Variable(Token t, Class memberOf) {
        super(t, memberOf);
        instanceID = classID;
        classID++;
    }

    public boolean isStatic() {
        return false;
    }

    @Override
    public Privacy getPrivacy() {
        return Privacy.publicS;
    }

    @Override
    public void checkDeclaration() throws CompilerException {}
}
