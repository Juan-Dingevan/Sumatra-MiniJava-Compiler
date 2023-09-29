package symboltable.symbols.classes;

import exceptions.general.CompilerException;
import exceptions.semantical.GenericsException;
import exceptions.semantical.MethodAlreadyExistsException;
import symboltable.symbols.Symbol;
import symboltable.symbols.members.Method;
import symboltable.symbols.members.Unit;
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
    protected HashMap<String, List<String>> childToParentGenericTypeMap;
    protected HashMap<String, List<String>> implementedGenericTypesMap;
    protected String inheritsFrom;
    protected Unit currentUnit;
    protected boolean hasBeenConsolidated;

    public Class(Token t) {
        super(t);

        methods = new HashMap<>();
        genericTypes = new ArrayList<>();
        parentDeclaredGenericTypes = new ArrayList<>();
        childToParentGenericTypeMap = new HashMap<>();
        implementedGenericTypesMap = new HashMap<>();
        currentUnit = null;
        hasBeenConsolidated = false;

        inheritsFrom = "";
    }

    public void setInheritance(Token t) {
        inheritsFrom = t.getLexeme();
    }

    public Unit getCurrentUnit() {
        return currentUnit;
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

    protected boolean isParentOrInterfaceDeclaredGenericType(String t) {
        return childToParentGenericTypeMap.get(t) != null || implementedGenericTypesMap.get(t) != null;
    }

    @SuppressWarnings("ReassignedVariable")
    public boolean referenceTypesAreEquivalentInClass(ReferenceType rt1, ReferenceType rt2) {
        int genericArity1 = rt1.getGenericTypes().size();
        int genericArity2 = rt2.getGenericTypes().size();

        if(genericArity1 != genericArity2)
            return false; //Caso trivial 2: uno tiene aridad generica y el otro no. No seran iguales nunca.

        var b = isParentOrInterfaceDeclaredGenericType(rt1.getReferenceName());
        var c = isParentOrInterfaceDeclaredGenericType(rt2.getReferenceName());

        var s = "";

        if(genericArity1 == 0) {
            if(rt1.equals(rt2))
                return true; //caso en el cual no hay genericidad, o la genericidad se resuelve trivialmente.

            //Caso en el que se comparan tipos parametricos "estandar". Por ejemplo,
            //comparar E con T, o String con A.
            return genericTypesAreEquivalentInClass(rt1.getReferenceName(), rt2.getReferenceName());
        }

        //Caso final, en el que comparamos tipos que tienen parametros de genericidad, por ejemplo
        //Tree<E> con Tree<T>, o Box<String> con Box<A>
        //Notar que casos como E<T> (tipo generico con aridad generica) nunca se dan.

        //Como base para la igualdad, tienen que tener el mismo nombre.
        boolean equivalent = rt1.getReferenceName().equals(rt2.getReferenceName());

        List<String> generics1 = rt1.getGenericTypes();
        List<String> generics2 = rt2.getGenericTypes();

        int i = 0;

        //Y luego, todos los parametros de tipo deben ser equivalentes entre si
        while(equivalent && i < genericArity1) {
            equivalent = genericTypesAreEquivalentInClass(generics1.get(i), generics2.get(i));
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
            if(childToParentGenericTypeMap.get(parentDeclaredGenericTypes.get(i)) == null)
                childToParentGenericTypeMap.put(parentDeclaredGenericTypes.get(i), new ArrayList<String>());

            childToParentGenericTypeMap.get(parentDeclaredGenericTypes.get(i)).add(parentGenericTypes.get(i));

            //childToParentGenericTypeMap.put(parentDeclaredGenericTypes.get(i), parentGenericTypes.get(i));
            System.out.println("in " + getName() + " mapping " + parentDeclaredGenericTypes.get(i) + " to " + parentGenericTypes.get(i));
        }

    }

    public void setParentDeclaredGenericTypes(List<String> parentDeclaredGenericTypes) {
        this.parentDeclaredGenericTypes = parentDeclaredGenericTypes;
    }

    public abstract void consolidate() throws CompilerException;
}
