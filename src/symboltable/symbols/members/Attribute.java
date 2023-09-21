package symboltable.symbols.members;

import exceptions.general.CompilerException;
import exceptions.semantical.GenericsException;
import exceptions.semantical.InvalidTypeException;
import exceptions.semantical.UndeclaredTypeException;
import symboltable.symbols.classes.Class;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.table.SymbolTable;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import token.Token;
import utility.StringUtilities;

public class Attribute extends TypedEntity {
    private static int classID = 0;
    private static final int LEVEL = 2;

    protected int instanceID;
    protected boolean isStatic;

    public static void resetID() {
        classID = 0;
    }

    public Attribute(Token t, Class memberOf) {
        super(t, memberOf);

        instanceID = classID;
        classID++;
    }

    @Override
    public void checkDeclaration() throws CompilerException {
        if(Type.isVoid(type))
            throw new InvalidTypeException(token, type);

        if(Type.isReferenceType(type)) {
            checkReferenceType(type);
        }
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
                 + " STATIC: " + isStatic + " PRIVACY: " + privacy;

        return s;
    }
}
