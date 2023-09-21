package symboltable.symbols.classes;

import exceptions.general.CompilerException;
import exceptions.semantical.*;
import symboltable.symbols.members.Attribute;
import symboltable.symbols.members.Constructor;
import symboltable.symbols.members.Method;
import symboltable.table.SymbolTable;
import token.Token;
import token.TokenType;
import utility.StringUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static token.TokenConstants.OBJECT_TOKEN;

public class ConcreteClass extends Class {
    private static int classID = 0;
    private static final int LEVEL = 1;

    protected int instanceID;
    protected HashMap<String, Attribute> attributes;
    protected List<String> interfaceDeclaredGenericTypes;
    protected Constructor constructor;
    protected String implementsInterface;

    public static void resetID() {
        classID = 0;
    }

    public ConcreteClass(Token t) {
        super(t);

        attributes = new HashMap<>();
        interfaceDeclaredGenericTypes = new ArrayList<>();

        constructor = null;
        implementsInterface = "";

        instanceID = classID;
        classID++;
    }

    public void setImplements(Token t) {
        implementsInterface = t.getLexeme();
    }

    protected boolean attributeExists(Attribute a) {
        return attributes.get(a.getName()) != null;
    }

    protected boolean constructorExists() {
        return constructor != null;
    }

    public void addAttribute(Attribute a) throws CompilerException{
        if(!attributeExists(a))
            attributes.put(a.getName(), a);
        else
            throw new AttributeAlreadyExistsException(a.getToken());
    }

    public void setConstructor(Constructor c) throws CompilerException {
        if(!constructorExists())
            constructor = c;
        else
            throw new ClassAlreadyHasConstructorException(c.getToken());
    }

    public void checkDeclaration() throws CompilerException {
        checkCircularInheritance();

        for(Attribute a : attributes.values())
            a.checkDeclaration();

        for(Method m : methods.values())
            m.checkDeclaration();

        if(constructor != null)
            constructor.checkDeclaration();
    }

    @SuppressWarnings("ReassignedVariable")
    protected void checkCircularInheritance() throws CircularInheritanceException{
        ConcreteClass currentClass = this;

        while(currentClass.getToken() != OBJECT_TOKEN) {
            String parentName = currentClass.inheritsFrom;

            if(getName().equals(parentName)) {
                throw new CircularInheritanceException(getToken());
            }

            currentClass = SymbolTable.getInstance().getClass(parentName);
        }

    }

    @Override
    public void consolidate() throws CompilerException {
        if(token == OBJECT_TOKEN)
            return;

        ConcreteClass parent = SymbolTable.getInstance().getClass(inheritsFrom);
        parent.consolidate();

        addInheritedAttributes(parent);
        addInheritedMethods(parent);

        checkCorrectImplementationOfInterface();

        checkGenerics();

        if(!constructorExists())
            setConstructor(Constructor.getDefaultConstructorForClass(this));
    }

    protected void checkGenerics() throws GenericsException {
        ConcreteClass parent = SymbolTable.getInstance().getClass(inheritsFrom);

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


    }

    protected void checkCorrectImplementationOfInterface() throws CompilerException {
        if(!implementsInterface.equals("")) {
            if(!SymbolTable.getInstance().interfaceExists(implementsInterface))
                throw new UndeclaredInterfaceImplementedException(getToken(), implementsInterface);

            Interface implementedInterface = SymbolTable.getInstance().getInterface(implementsInterface);
            implementedInterface.consolidate();

            for(Method interfaceMethod : implementedInterface.getMethods())
                if(methodExists(interfaceMethod)) {
                    Method implementedMethod = getMethod(interfaceMethod.getName());
                    if(!implementedMethod.hasSameSignature(interfaceMethod))
                        throw new IncorrectlyOverwrittenMethodException(implementedMethod.getToken(), getToken());
                } else {
                    throw new UnimplementedMethodException(interfaceMethod.getToken(), implementedInterface.getToken());
                }
        }
    }

    private void addInheritedMethods(ConcreteClass parent) throws CompilerException {
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

    protected void addInheritedAttributes(ConcreteClass parent) throws CompilerException {
        for(Attribute parentAttribute : parent.getAttributes()) {
            if (!attributeExists(parentAttribute)) {
                addAttribute(parentAttribute);
            } else {
                Token rewrittenToken = attributes.get(parentAttribute.getName()).getToken();
                Token originalToken = parentAttribute.getToken();
                throw new OverwrittenAttributeException(rewrittenToken, originalToken);
            }
        }
    }

    public Iterable<Attribute> getAttributes() {
        return attributes.values();
    }

    public List<String> getInterfaceDeclaredGenericTypes() {
        return interfaceDeclaredGenericTypes;
    }

    public void setInterfaceDeclaredGenericTypes(List<String> interfaceDeclaredGenericTypes) {
        this.interfaceDeclaredGenericTypes = interfaceDeclaredGenericTypes;
    }

    public String toString() {
        String name = this.getName();
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "CLASS{" + instanceID + "}: " + name + " EXTENDS: " + inheritsFrom + " IMPLEMENTS: " + implementsInterface + "\n";

        if(genericTypes.size() > 0) {
            s += prefix + "GENERICS\n";
            for(String g : genericTypes)
                s += prefix + prefix + g + "\n";
        }

        if(parentDeclaredGenericTypes.size() > 0) {
            s += prefix + "PARENT DECLARED GENERICS\n";
            for(String g : parentDeclaredGenericTypes)
                s += prefix + prefix + g + "\n";
        }

        if(interfaceDeclaredGenericTypes.size() > 0) {
            s += prefix + "INTERFACE DECLARED GENERICS\n";
            for(String g : interfaceDeclaredGenericTypes)
                s += prefix + prefix + g + "\n";
        }

        s += prefix + "ATTRIBUTES:\n";

        for(Attribute a : attributes.values())
            s += a.toString() + "\n";

        s += prefix + "METHODS:\n";

        for(Method m : methods.values())
            s += m.toString();

        s += prefix + "CONSTRUCTOR:\n";
        s += (constructor == null ? "" : constructor.toString());

        return s;
    }
}
