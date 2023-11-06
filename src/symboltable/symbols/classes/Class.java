package symboltable.symbols.classes;

import exceptions.general.CompilerException;
import exceptions.semantical.*;
import exceptions.semantical.declaration.GenericsException;
import exceptions.semantical.declaration.MethodAlreadyExistsException;
import symboltable.symbols.Symbol;
import symboltable.symbols.members.Member;
import symboltable.symbols.members.Method;
import symboltable.symbols.members.Unit;
import symboltable.table.SymbolTable;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import token.Token;
import token.TokenType;

import java.util.*;

import static token.TokenConstants.OBJECT_TOKEN;

public abstract class Class extends Symbol {
    protected HashMap<String, Method> methods;
    protected List<String> genericTypes;
    protected List<String> parentDeclaredGenericTypes;
    protected HashMap<String, List<String>> childToParentGenericTypeMap;
    protected HashMap<String, List<String>> implementedGenericTypesMap;
    protected String inheritsFrom;
    protected Unit currentUnit;
    protected boolean hasBeenConsolidated;
    protected int nextMethodOffset;
    protected int nextAttributeOffset;

    public Class(Token t) {
        super(t);

        methods = new HashMap<>();
        genericTypes = new ArrayList<>();
        parentDeclaredGenericTypes = new ArrayList<>();
        childToParentGenericTypeMap = new HashMap<>();
        implementedGenericTypesMap = new HashMap<>();
        currentUnit = null;
        hasBeenConsolidated = false;

        nextMethodOffset = Member.METHOD_MIN_OFFSET;
        nextAttributeOffset = Member.ATTRIBUTE_MIN_OFFSET;

        inheritsFrom = "";
    }

    public void setInheritance(Token t) {
        inheritsFrom = t.getLexeme();
    }

    public Unit getCurrentUnit() {
        return currentUnit;
    }

    public int getNextMethodOffset() {
        return nextMethodOffset;
    }

    public int getNextAttributeOffset() {
        return nextAttributeOffset;
    }

    protected boolean methodExists(Method m) {
        return methods.get(m.getName()) != null;
    }

    public Method getMethod(String name) {
        return methods.get(name);
    }
    public Collection<Method> getMethods() {
        return methods.values();
    }

    public void addMethod(Method m) throws CompilerException {
        if(!methodExists(m)) {
            methods.put(m.getName(), m);
            currentUnit = m;
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

    public boolean isGenericallyInstantiated(String instantiatedType) {
        Map<String, List<String>> map;

        //We can either have inheritance OR implementation, thus, we get the map
        //accordingly
        if(inheritsFrom.equals(OBJECT_TOKEN.getLexeme())) {
            map = implementedGenericTypesMap;
        } else {
            map = childToParentGenericTypeMap;
        }

        for(Map.Entry<String, List<String>> e : map.entrySet()) {
            List<String> mappedTypes = e.getValue();
            for(String mappedType : mappedTypes) {
                if(mappedType.equals(instantiatedType)) {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean isParentOrInterfaceDeclaredGenericType(String t) {
        return childToParentGenericTypeMap.get(t) != null || implementedGenericTypesMap.get(t) != null;
    }

    public String getInheritsFrom() {
        return inheritsFrom;
    }

    @SuppressWarnings("ReassignedVariable")
    protected abstract void checkCircularInheritance() throws SemanticException;

    public abstract boolean isDescendantOf(Class c);

    public abstract void consolidate() throws CompilerException;

    /*
    *   GENERICS
    */

    @SuppressWarnings("ReassignedVariable")
    public boolean referenceTypesAreEquivalentInClass(ReferenceType rt1, ReferenceType rt2) {
        int genericArity1 = rt1.getGenericTypes().size();
        int genericArity2 = rt2.getGenericTypes().size();

        if(genericArity1 != genericArity2)
            return false;

        if(genericArity1 == 0) {
            if(rt1.equals(rt2))
                return true; //Either theres no Generics, or they get resolved trivially (Tree<E> implements Iterable<E>)

            return genericTypesAreEquivalentInClass(rt1.getReferenceName(), rt2.getReferenceName());
        }

        boolean equivalent = rt1.getReferenceName().equals(rt2.getReferenceName());

        List<String> generics1 = rt1.getGenericTypes();
        List<String> generics2 = rt2.getGenericTypes();

        int i = 0;

        while(equivalent && i < genericArity1) {
            equivalent = genericTypesAreEquivalentInClass(generics1.get(i), generics2.get(i));
            i++;
        }

        return equivalent;
    }

    public boolean referenceTypesConformInClass(ReferenceType rt1, ReferenceType rt2) {
        int genericArity1 = rt1.getGenericTypes().size();
        int genericArity2 = rt2.getGenericTypes().size();

        if(genericArity1 != genericArity2)
            return false;

        Class c1 = SymbolTable.getInstance().getClassOrInterface(rt1.getReferenceName());
        Class c2 = SymbolTable.getInstance().getClassOrInterface(rt2.getReferenceName());

        boolean equivalent = c1.isDescendantOf(c2);

        List<String> generics1 = rt1.getGenericTypes();
        List<String> generics2 = rt2.getGenericTypes();

        int i = 0;

        while(equivalent && i < genericArity1) {
            String type1 = generics1.get(i);
            String type2 = generics2.get(i);
            equivalent = type1.equals(type2) || genericTypesAreEquivalentInClass(type1, type2);
            i++;
        }

        return equivalent;
    }

    @SuppressWarnings("ReassignedVariable")
    public boolean genericTypesAreEquivalentInClass(String childType, String queriedType) {
        List<String> mappedTypes;

        //We can either have inheritance OR implementation, thus, we get the map
        //accordingly
        if(inheritsFrom.equals(OBJECT_TOKEN.getLexeme())) {
            mappedTypes = implementedGenericTypesMap.get(childType);
        } else {
            mappedTypes = childToParentGenericTypeMap.get(childType);
        }

        Class parent = SymbolTable.getInstance().getClassOrInterface(inheritsFrom);

        //If we didnt have that attribute mapped, we query if our father has it.
        if(mappedTypes == null)
            //if we're Object, we dont have a father, so we return false
            if(token == OBJECT_TOKEN)
                return false;
            //else, we do the query
            else
                return parent.genericTypesAreEquivalentInClass(childType, queriedType);

        //if we DID have something mapped, we check if it directly maps to the queried type
        //or if it's equivalent as per our father's side.
        boolean match = false;
        String mappedType;
        for(int i = 0; !match && i < mappedTypes.size(); i++) {
            mappedType = mappedTypes.get(i);
            match = mappedType.equals(queriedType) || parent.genericTypesAreEquivalentInClass(mappedType, queriedType);
        }

        return match;
    }

    public String instantiateGenericType(String genericType) {
        Map<String, List<String>> map;
        Class parent = SymbolTable.getInstance().getClassOrInterface(inheritsFrom);

        //We can either have inheritance OR implementation, thus, we get the map
        //accordingly
        if(inheritsFrom.equals(OBJECT_TOKEN.getLexeme())) {
            map = implementedGenericTypesMap;
        } else {
            map = childToParentGenericTypeMap;
        }

        String key = null;

        for(Map.Entry<String, List<String>> e : map.entrySet()) {
            List<String> mappedTypes = e.getValue();
            for(String mappedType : mappedTypes) {
                if(mappedType.equals(genericType)) {
                    key = e.getKey();
                    break;
                }
            }
        }

        //If we didnt have that attribute mapped, we query if our father has it.
        if(key == null)
            //if we're Object, we dont have a father, so we return false
            if(token == OBJECT_TOKEN)
                return null;
            //else, we do the query
            else
                return parent.instantiateGenericType(genericType);

        return key;
    }

    protected void checkGenerics() throws GenericsException {
        Class parent = SymbolTable.getInstance().getClassOrInterface(inheritsFrom);

        genericTypeParametersArentDeclaredNames();
        extendsTypeParametersArityMatchesParentsRealTypeParameterArity(parent);
        extendsTypeParametersAreDeclaredClassesOrDeclaredTypeParameters();
        createChildToParentTypeParameterMap(parent);
    }

    private void createChildToParentTypeParameterMap(Class parent) {
        List<String> parentGenericTypes = parent.getGenericTypes();
        for(int i = 0; i < parentDeclaredGenericTypes.size(); i++) {
            if(childToParentGenericTypeMap.get(parentDeclaredGenericTypes.get(i)) == null)
                childToParentGenericTypeMap.put(parentDeclaredGenericTypes.get(i), new ArrayList<String>());

            childToParentGenericTypeMap.get(parentDeclaredGenericTypes.get(i)).add(parentGenericTypes.get(i));
        }
    }

    private void extendsTypeParametersAreDeclaredClassesOrDeclaredTypeParameters() throws GenericsException {
        for(String parentGenericType : parentDeclaredGenericTypes) {
            if(!(SymbolTable.getInstance().exists(parentGenericType) || genericTypes.contains(parentGenericType))) {
                Token genericsToken = new Token(TokenType.id_class, parentGenericType, getToken().getLineNumber());
                String errorMessage = "The parametric type " + parentGenericType + " is not valid in that context.";
                throw new GenericsException(genericsToken, errorMessage);
            }
        }
    }

    private void extendsTypeParametersArityMatchesParentsRealTypeParameterArity(Class parent) throws GenericsException {
        if(parentDeclaredGenericTypes.size() != parent.getGenericTypes().size()) {
            String errorMessage = "When the class " + getName() + " declares extension of " + parent.getName()
                    + ", there is a mismatch of number of generic type parameters (" + parentDeclaredGenericTypes.size()
                    + " but " + parent.getName() + " has " + parent.getGenericTypes().size() + ")";
            throw new GenericsException(getToken(), errorMessage);
        }
    }

    private void genericTypeParametersArentDeclaredNames() throws GenericsException {
        for(String genericType : genericTypes) {
            if(SymbolTable.getInstance().exists(genericType)) {
                Token genericsToken = new Token(TokenType.id_class, genericType, getToken().getLineNumber());
                String errorMessage = "Error: the generic type " + genericType + " of class " + getName() + " shares a name with a class or interface.";
                throw new GenericsException(genericsToken, errorMessage);
            }
        }
    }

    public void setParentDeclaredGenericTypes(List<String> parentDeclaredGenericTypes) {
        this.parentDeclaredGenericTypes = parentDeclaredGenericTypes;
    }
}
