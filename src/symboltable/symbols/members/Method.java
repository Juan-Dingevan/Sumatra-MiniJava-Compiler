package symboltable.symbols.members;

import exceptions.general.CompilerException;
import exceptions.semantical.GenericsException;
import exceptions.semantical.UndeclaredTypeException;
import symboltable.symbols.classes.Class;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.table.SymbolTable;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import token.Token;
import utility.StringUtilities;

import java.util.List;

public class Method extends Unit {
    private static int classID = 0;
    private static final int LEVEL = 2;

    protected int instanceID;
    protected Type returnType;
    protected boolean isStatic;

    public static void resetID() {
        classID = 0;
    }

    public Method(Token t, Class memberOf) {
        super(t, memberOf);

        instanceID = classID;
        classID++;
    }

    @Override
    public void checkDeclaration() throws CompilerException {
        if(ReferenceType.isReferenceType(returnType))
            checkReferenceType(returnType);

        super.checkDeclaration();
    }

    protected void checkReferenceType(Type type) throws UndeclaredTypeException, GenericsException {
        ReferenceType rt = (ReferenceType) type;

        if(!SymbolTable.getInstance().exists(rt.getReferenceName()) && !memberOf.isGenericType(rt.getReferenceName()))
            throw new UndeclaredTypeException(token, type);

        ConcreteClass classOfType = SymbolTable.getInstance().getClass(rt.getReferenceName());
        if(classOfType != null && rt.getGenericTypes().size() != classOfType.getGenericTypes().size()) {
            String errorMessage = "The member " + getName() + " (declared in line " + getToken().getLineNumber() + ") of type "
                    + rt + " doesn't have the correct amount of generic parameters (expected "
                    + classOfType.getGenericTypes().size() + " but got " + rt.getGenericTypes().size() + ")";
            throw new GenericsException(getToken(), errorMessage);
        }

        for(String genericType : rt.getGenericTypes()) {
            if(!SymbolTable.getInstance().exists(genericType) && !memberOf.isGenericType(genericType)) {
                String errorMessage = "The member " + getName() + " (declared in line " + getToken().getLineNumber() + ") of type "
                        + rt + " uses the following invalid parametric type: " + genericType;
                throw new GenericsException(getToken(), errorMessage);
            }
        }
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
    public boolean isMainMethod() {
        boolean name = getName().equals("main");
        boolean staticity = isStatic();
        boolean parameters = parameterMap.size() == 0;

        return name && staticity && parameters;
    }

    @SuppressWarnings("ReassignedVariable")
    public boolean hasSameSignature(Method m) {
        boolean sameName = getName().equals(m.getName());
        boolean sameStaticity = isStatic() == m.isStatic();
        boolean samePrivacy = getPrivacy() == m.getPrivacy();

        boolean sameType = typesAreEquivalent(getReturnType(), m.getReturnType());

        List<Parameter> parameters1 = getParameters();
        List<Parameter> parameters2 = m.getParameters();

        boolean sameParameters = parameters1.size() == parameters2.size();
        int n = parameters1.size();
        int i = 0;

        Parameter p1;
        Parameter p2;

        while(sameParameters && i < n) {
            p1 = parameters1.get(i);
            p2 = parameters2.get(i);
            sameParameters = typedEntitiesAreEquivalent(p1, p2);
            i++;
        }

        return sameParameters && sameType && samePrivacy && sameStaticity && sameName;
    }
    public boolean typedEntitiesAreEquivalent(TypedEntity te1, TypedEntity te2) {
        Type t1 = te1.getType();
        Type t2 = te2.getType();

        return typesAreEquivalent(t1, t2);
    }

    private boolean typesAreEquivalent(Type t1, Type t2) {
        if(Type.isReferenceType(t1) != Type.isReferenceType(t2))
            return false; //uno es ref-type y el otro no, trivialmente son distintos

        if(!Type.isReferenceType(t1))
            return t1.equals(t2); //caso trivial 1: ninguno es ref type

        //a partir de aca podemos asumir que ambos son ref-type
        //hacemos el cast y trabajamos con los rt directamente
        ReferenceType rt1 = (ReferenceType) t1;
        ReferenceType rt2 = (ReferenceType) t2;

        return memberOf.referenceTypesAreEquivalentInClass(rt1, rt2);
    }

    public String toString() {
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "METHOD{" + instanceID + "}: " + getName() + " RETURN TYPE: " + returnType
                 + " STATIC: " + isStatic + " PRIVACY: " + privacy + "\n";
        s += prefix + "PARAMETERS:\n";

        for(Parameter p : parameterMap.values())
            s += p.toString() + "\n";

        return s;
    }
}
