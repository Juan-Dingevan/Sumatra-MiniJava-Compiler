package symboltable.symbols.classes;

import exceptions.general.CompilerException;
import exceptions.semantical.SemanticException;
import exceptions.semantical.declaration.CircularInheritanceException;
import exceptions.semantical.declaration.IncorrectlyOverwrittenMethodException;
import exceptions.semantical.declaration.StaticMethodInInterfaceException;
import exceptions.semantical.declaration.UndeclaredExtendsException;
import symboltable.symbols.members.Method;
import symboltable.table.SymbolTable;
import token.Token;
import utility.StringUtilities;

import java.util.Collection;
import java.util.HashMap;

import static token.TokenConstants.OBJECT_TOKEN;

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

    @SuppressWarnings("ReassignedVariable")
    protected void checkCircularInheritance() throws SemanticException {
        Class currentClass = this;

        while(!currentClass.getInheritsFrom().equals("")) {
            String parentName = currentClass.getInheritsFrom();

            if(getName().equals(parentName)) {
                throw new CircularInheritanceException(getToken());
            }

            currentClass = SymbolTable.getInstance().getInterface(parentName);

            if(currentClass == null)
                throw new UndeclaredExtendsException(getToken(), parentName);
        }

    }

    @Override
    public boolean isDescendantOf(Class possibleAncestor) {
        String possibleAncestorName = possibleAncestor.getName();

        boolean sameClass = getName().equals(possibleAncestorName);

        if (sameClass) {
            return true;
        }

        if(inheritsFrom.equals("")) {
            return false;
        }

        Class parent = SymbolTable.getInstance().getInterface(inheritsFrom);

        return parent.isDescendantOf(possibleAncestor);
    }

    @Override
    public void checkDeclaration() throws CompilerException {
        checkCircularInheritance();

        boolean noParent = inheritsFrom.equals("");

        for(Method m : methods.values()) {
            m.checkDeclaration();

            if(m.isStatic())
                throw new StaticMethodInInterfaceException(m.getToken(), getToken());

            if(noParent) {
                m.setOffset(nextMethodOffset);
                nextMethodOffset++;
            }
        }
    }

    @Override
    public void consolidate() throws CompilerException {
        if(hasBeenConsolidated)
            return;

        if(inheritsFrom.equals("")) {
            setOffsets();
            hasBeenConsolidated = true;
            return;
        }

        Interface parent = SymbolTable.getInstance().getInterface(inheritsFrom);
        parent.consolidate();

        checkGenerics();

        for(Method parentMethod : parent.getMethods()) {
            if(!methodExists(parentMethod)) {
                addMethod(parentMethod);
            } else {
                Method childMethod = getMethod(parentMethod.getName());
                if(childMethod.hasSameSignature(parentMethod)) {
                    int parentMethodOffset = parentMethod.getOffset();
                    childMethod.setOffset(parentMethodOffset);
                    childMethod.setRedefined(true);
                } else {
                    throw new IncorrectlyOverwrittenMethodException(childMethod.getToken(), getToken());
                }
            }
        }

        setOffsets();

        hasBeenConsolidated = true;
    }

    @SuppressWarnings("ReassignedVariable")
    private void setOffsets() {
        int nextOffset;
        if(inheritsFrom.equals("")) {
            nextOffset = 0;
        } else {
            Interface parent = SymbolTable.getInstance().getInterface(inheritsFrom);
            nextOffset = parent.getNextMethodOffset();
        }

        for(Method method : getMethods()) {
            boolean isRedefined = method.isRedefined();
            boolean isInherited = method.getMemberOf() != this;

            boolean needsOffset = !(isRedefined || isInherited);

            if(needsOffset) {
                System.out.println("in " + getName() + " adding offset " + nextOffset + " to met " + method.getName());
                method.setOffset(nextOffset);
                nextOffset++;
            }
        }

        nextMethodOffset = nextOffset;
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
