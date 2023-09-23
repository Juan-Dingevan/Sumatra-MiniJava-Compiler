package symboltable.symbols.classes;

import exceptions.general.CompilerException;
import exceptions.semantical.GenericsException;
import exceptions.semantical.MethodAlreadyExistsException;
import symboltable.symbols.Symbol;
import symboltable.symbols.members.Method;
import symboltable.symbols.members.Parameter;
import symboltable.table.SymbolTable;
import symboltable.types.ReferenceType;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static token.TokenConstants.OBJECT_TOKEN;

public abstract class Class extends Symbol {
    protected HashMap<String, Method> methods;
    protected List<String> genericTypes;
    protected List<String> parentDeclaredGenericTypes;
    protected HashMap<String, String> childToParentGenericTypeMap;
    protected HashMap<String, String> implementedGenericTypesMap;
    protected String inheritsFrom;
    protected Method currentMethod;
    protected boolean hasBeenConsolidated;

    public Class(Token t) {
        super(t);

        methods = new HashMap<>();
        genericTypes = new ArrayList<>();
        parentDeclaredGenericTypes = new ArrayList<>();
        childToParentGenericTypeMap = new HashMap<>();
        implementedGenericTypesMap = new HashMap<>();
        currentMethod = null;
        hasBeenConsolidated = false;

        inheritsFrom = "";
    }

    public void setInheritance(Token t) {
        inheritsFrom = t.getLexeme();
    }

    public Method getCurrentMethod() {
        return currentMethod;
    }

    protected boolean methodExists(Method m) {
        return methods.get(m.getName()) != null;
    }

    protected Method getMethod(String name) {
        return methods.get(name);
    }
    public Iterable<Method> getMethods() {
        return methods.values();
    }

    public void addMethod(Method m) throws CompilerException {
        if(!methodExists(m)) {
            methods.put(m.getName(), m);
            currentMethod = m;
        } else
            throw new MethodAlreadyExistsException(m.getToken());
    }

    public List<String> getGenericTypes() {
        return genericTypes;
    }
    public void setGenericTypes(List<String> genericTypes) {
        this.genericTypes = genericTypes;
    }
    public boolean isGenericType(String genericType) {
        return genericTypes.contains(genericType);
    }

    @SuppressWarnings("ReassignedVariable")
    public boolean referenceTypesAreEquivalent(ReferenceType rt1, ReferenceType rt2) {
        int genericArity1 = rt1.getGenericTypes().size();
        int genericArity2 = rt2.getGenericTypes().size();

        if(genericArity1 != genericArity2)
            return false;

        if(genericArity1 == 0)
            return genericTypesAreEquivalent(rt1.getReferenceName(), rt2.getReferenceName());

        List<String> generics1 = rt1.getGenericTypes();
        List<String> generics2 = rt2.getGenericTypes();

        boolean equivalent = true;
        int i = 0;

        while(equivalent && i < genericArity1) {
            equivalent = genericTypesAreEquivalent(generics1.get(i), generics2.get(i));
            i++;
        }

        return equivalent;
    }

    @SuppressWarnings("ReassignedVariable")
    public boolean genericTypesAreEquivalent(String childType, String queriedType) {
        String mappedType = childToParentGenericTypeMap.get(childType);

        if(mappedType == null)
            mappedType = implementedGenericTypesMap.get(childType);

        if(mappedType.equals(queriedType))
            return true;

        if(token == OBJECT_TOKEN)
            return false;

        Class parent = SymbolTable.getInstance().getClassOrInterface(inheritsFrom);
        return parent.genericTypesAreEquivalent(mappedType, queriedType);
    }

    protected void checkGenerics() throws GenericsException {
        Class parent = SymbolTable.getInstance().getClassOrInterface(inheritsFrom);

        //declared parameter types do not share name with other classes or interfaces
        for(String genericType : genericTypes) {
            if(SymbolTable.getInstance().exists(genericType)) {
                Token genericsToken = new Token(TokenType.id_class, genericType, getToken().getLineNumber());
                String errorMessage = "Error: the generic type " + genericType + " of class " + getName() + " shares a name with a class or interface.";
                throw new GenericsException(genericsToken, errorMessage);
            }
        }

        /*declared parameter types of the parent class have the correct arity
         * prevents following case:
         * class A<K, V> {...}
         * class B<K> extends A<K> {...}
         * */
        if(parentDeclaredGenericTypes.size() != parent.getGenericTypes().size()) {
            String errorMessage = "When the class " + getName() + " declares extension of " + parent.getName()
                    + ", there is a mismatch of number of generic type parameters (" + parentDeclaredGenericTypes.size()
                    + " but " + parent.getName() + " has " + parent.getGenericTypes().size() + ")";
            throw new GenericsException(getToken(), errorMessage);
        }

        /*declared parent parameters are either declared classes or interfaces, or parametric types of child
         * prevents following case:
         * class A<K, V> {...}
         * class B<K, V> extends A<Q, K> {...} (Q doesn't exist)
         * and
         * class A<K, V> {...}
         * class B<K> extends A<K, V> {...} (B doesn't have enough type parameters)
         * */
        for(String parentGenericType : parentDeclaredGenericTypes) {
            if(!(SymbolTable.getInstance().exists(parentGenericType) || genericTypes.contains(parentGenericType))) {
                Token genericsToken = new Token(TokenType.id_class, parentGenericType, getToken().getLineNumber());
                String errorMessage = "The parametric type " + parentGenericType + " is not valid in that context.";
                throw new GenericsException(genericsToken, errorMessage);
            }
        }

        //At this point, we can assume that (at least arity-wise), the parameters are well-defined.
        List<String> parentGenericTypes = parent.getGenericTypes();
        for(int i = 0; i < parentDeclaredGenericTypes.size(); i++) {
            childToParentGenericTypeMap.put(parentDeclaredGenericTypes.get(i), parentGenericTypes.get(i));
            System.out.println("in " + getName() + " mapping " + parentDeclaredGenericTypes.get(i) + " to " + parentGenericTypes.get(i));
        }

    }

    public List<String> getParentDeclaredGenericTypes() {
        return parentDeclaredGenericTypes;
    }

    public void setParentDeclaredGenericTypes(List<String> parentDeclaredGenericTypes) {
        this.parentDeclaredGenericTypes = parentDeclaredGenericTypes;
    }

    public abstract void consolidate() throws CompilerException;
}
