package symboltable.symbols.members;

import exceptions.general.CompilerException;
import exceptions.semantical.InvalidTypeException;
import exceptions.semantical.UndeclaredTypeException;
import symboltable.table.SymbolTable;
import symboltable.symbols.classes.Class;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import token.Token;
import utility.StringUtilities;

public class Parameter extends TypedEntity {
    private static int classID = 0;
    private static final int LEVEL = 3;
    protected int instanceID;

    public static void resetID() {
        classID = 0;
    }

    public Parameter(Token t, Class memberOf) {
        super(t, memberOf);
        instanceID = classID;
        classID++;
    }

    @Override
    public void checkDeclaration() throws CompilerException {
        if(Type.isVoid(type))
            throw new InvalidTypeException(token, type);

        if(ReferenceType.isReferenceType(type))
            checkReferenceType(type);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Parameter) {
            Parameter p = (Parameter) obj;
            return getName().equals(p.getName()) && getType().equals(p.getType());
        } else {
            return false;
        }
    }

    public String toString() {
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "PARAMETER{"+instanceID+"}: " + getName() + " TYPE: " + type;
        return s;
    }
}
