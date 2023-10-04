package symboltable.symbols.classes;

import exceptions.general.CompilerException;
import exceptions.semantical.declaration.IncorrectlyOverwrittenMethodException;
import exceptions.semantical.declaration.StaticMethodInInterfaceException;
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
        checkCircularInheritance();

        for(Method m : methods.values()) {
            m.checkDeclaration();
            if(m.isStatic())
                throw new StaticMethodInInterfaceException(m.getToken(), getToken());
        }
    }

    @Override
    public void consolidate() throws CompilerException {
        if(hasBeenConsolidated || inheritsFrom.equals(""))
            return;

        Interface parent = SymbolTable.getInstance().getInterface(inheritsFrom);
        parent.consolidate();

        checkGenerics();

        for(Method parentMethod : parent.getMethods()) {
            if(!methodExists(parentMethod)) {
                addMethod(parentMethod);
            } else {
                Method childMethod = getMethod(parentMethod.getName());
                if(!childMethod.hasSameSignature(parentMethod))
                    throw new IncorrectlyOverwrittenMethodException(childMethod.getToken(), getToken());
            }
        }

        hasBeenConsolidated = true;
    }

    public String toString() {
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "INTERFACE{"+instanceID+"}: " + getName() + " EXTENDS: " + inheritsFrom + "\n";

        if(genericTypes.size() > 0) {
            s += prefix + "GENERICS\n";
            for(String g : genericTypes)
                s += prefix + prefix + g + "\n";
        }

        if(parentDeclaredGenericTypes.size() > 0) {
            s += prefix + "PARENT DECLARED GENERICS\n";
            for(String g : parentDeclaredGenericTypes)
                s += prefix + prefix + g + " maps to " + childToParentGenericTypeMap.get(g) + "\n";
        }

        s+= prefix + "METHOD HEADERS:\n";

        for(Method m : methods.values())
            s += m.toString();

        return s;
    }
}
