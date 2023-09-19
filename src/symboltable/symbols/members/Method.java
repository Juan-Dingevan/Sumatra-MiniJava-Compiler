package symboltable.symbols.members;

import exceptions.general.CompilerException;
import exceptions.semantical.InvalidTypeException;
import exceptions.semantical.UndeclaredTypeException;
import symboltable.table.SymbolTable;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import token.Token;
import utility.StringUtilities;

import java.util.HashMap;

public class Method extends Unit {
    private static int classID = 0;
    private static final int LEVEL = 2;

    protected int instanceID;
    protected Type returnType;
    protected boolean isStatic;

    public Method(Token t) {
        super(t);

        instanceID = classID;
        classID++;
    }

    @Override
    public void checkDeclaration() throws CompilerException {
        if(Type.isReferenceType(returnType)) {
            ReferenceType rt = (ReferenceType) returnType;
            if(!SymbolTable.getInstance().exists(rt.getReferenceName()))
                throw new UndeclaredTypeException(token, returnType);
        }

        super.checkDeclaration();
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
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
        s += " STATIC: " + isStatic + " PRIVACY: " + privacy + "\n";
        s += prefix + "PARAMETERS:\n";

        for(Parameter p : parameters.values())
            s += p.toString() + "\n";

        return s;
    }
}
