package symboltable.symbols.members;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import exceptions.semantical.declaration.GenericsException;
import exceptions.semantical.declaration.UndeclaredTypeException;
import symboltable.symbols.classes.Class;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.table.SymbolTable;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import symboltable.types.Void;
import token.Token;
import utility.StringUtilities;

import java.util.List;

public class Method extends Unit {
    public static int classID = 0;
    private static final int LEVEL = 2;

    protected int instanceID;
    protected Type returnType;
    protected boolean isStatic;
    protected boolean isRedefined;


    public static void resetID() {
        classID = 0;
    }

    public Method(Token t, Class memberOf) {
        super(t, memberOf);

        instanceID = classID;
        classID++;

        isRedefined = false;
    }

    @Override
    public void checkDeclaration() throws CompilerException {
        if(ReferenceType.isReferenceType(returnType))
            checkReferenceType(returnType);

        super.checkDeclaration();
    }

    @Override
    public String getTag() {
        return CodeGenerator.getMethodTag(this);
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

    @Override
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
        boolean type = getReturnType().equals(new Void());
        boolean parameters = parameterMap.size() == 0;

        return name && staticity && type && parameters;
    }

    @SuppressWarnings("ReassignedVariable")
    public boolean hasSameSignature(Method m) {
        boolean sameName = getName().equals(m.getName());
        boolean sameStaticity = isStatic() == m.isStatic();
        boolean samePrivacy = getPrivacy() == m.getPrivacy();

        boolean sameType = Type.typesAreEquivalentInContext(getReturnType(), m.getReturnType(), memberOf);

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
            sameParameters = Type.typedEntitiesAreEquivalentInContext(p1, p2, memberOf);
            i++;
        }

        return sameParameters && sameType && samePrivacy && sameStaticity && sameName;
    }

    public boolean isRedefined() {
        return isRedefined;
    }

    public void setRedefined(boolean redefined) {
        isRedefined = redefined;
    }

    public String toString() {
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "METHOD{" + instanceID + "}: " + getName() + " RETURN TYPE: " + returnType
                 + " STATIC: " + isStatic + " PRIVACY: " + privacy + " OFFSET: " + offset + "\n";
        s += prefix + "PARAMETERS:\n";

        for(Parameter p : parameterMap.values())
            s += p.toString() + "\n";

        return s;
    }
}
