package symboltable.symbols.classes;

import exceptions.general.CompilerException;
import exceptions.semantical.IncorrectlyOverwrittenMethodException;
import exceptions.semantical.StaticMethodInInterfaceException;
import symboltable.symbols.members.Method;
import symboltable.table.SymbolTable;
import token.Token;
import utility.StringUtilities;

import java.util.HashMap;

public class Interface extends Class {
    private static int classID = 0;
    private static final int LEVEL = 1;

    protected int instanceID;

    public static void resetID() {
        classID = 0;
    }

    public Interface(Token t) {
        super(t);

        instanceID = classID;

        methods = new HashMap<>();

        classID++;
    }

    @Override
    public void checkDeclaration() throws CompilerException {
        for(Method m : methods.values()) {
            m.checkDeclaration();
            if(m.isStatic())
                throw new StaticMethodInInterfaceException(m.getToken(), getToken());
        }
    }

    @Override
    public void consolidate() throws CompilerException {
        if(inheritsFrom.equals(""))
            return;

        Interface parent = SymbolTable.getInstance().getInterface(inheritsFrom);
        parent.consolidate();

        for(Method parentMethod : parent.getMethods()) {
            if(!methodExists(parentMethod)) {
                addMethod(parentMethod);
            } else {
                Method childMethod = getMethod(parentMethod.getName());
                if(!childMethod.hasSameSignature(parentMethod))
                    throw new IncorrectlyOverwrittenMethodException(childMethod.getToken(), getToken());
            }
        }
    }

    public String toString() {
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "INTERFACE{"+instanceID+"}: " + getName() + " EXTENDS: " + inheritsFrom + "\n";

        s+="METHOD HEADERS:\n";

        for(Method m : methods.values())
            s += m.toString() + "\n";

        return s;
    }
}
