package symboltable.symbols.members;

import exceptions.semantical.GenericsException;
import exceptions.semantical.UndeclaredTypeException;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.table.SymbolTable;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import symboltable.symbols.classes.Class;
import token.Token;

public abstract class TypedEntity extends Member{
    protected Type type;
    public TypedEntity(Token t, Class memberOf) {
        super(t, memberOf);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
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
}
