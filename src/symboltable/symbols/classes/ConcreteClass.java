package symboltable.symbols.classes;

import exceptions.general.CompilerException;
import exceptions.semantical.declaration.*;
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
        implementedGenericTypesMap = new HashMap<>();

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
        if(!constructorExists()) {
            constructor = c;
            currentUnit = c;
        } else
            throw new ClassAlreadyHasConstructorException(c.getToken());
    }

    public void checkDeclaration() throws CompilerException {
        checkCircularInheritance();

        if(!implementsInterface.equals(""))
            if (!SymbolTable.getInstance().interfaceExists(implementsInterface))
                throw new UndeclaredInterfaceImplementedException(getToken(), implementsInterface);

        for(Attribute a : attributes.values())
            a.checkDeclaration();

        for(Method m : methods.values()) {
            m.checkDeclaration();
            if(m.isMainMethod())
                SymbolTable.getInstance().addMainMethod(m);
        }

        if(constructor != null)
            constructor.checkDeclaration();
    }

    @Override
    public void consolidate() throws CompilerException {
        if(hasBeenConsolidated || token == OBJECT_TOKEN)
            return;

        ConcreteClass parent = SymbolTable.getInstance().getClass(inheritsFrom);
        parent.consolidate();

        if(implementsInterface.equals(""))
            implementsInterface = parent.getImplementedInterface();

        checkGenerics();

        addInheritedAttributes(parent);
        addInheritedMethods(parent);

        checkCorrectImplementationOfInterface();

        if(!constructorExists())
            setConstructor(Constructor.getDefaultConstructorForClass(this));

        hasBeenConsolidated = true;
    }

    protected String getImplementedInterface() {
        return implementsInterface;
    }

    protected void checkCorrectImplementationOfInterface() throws CompilerException {
        if(!implementsInterface.equals("")) {
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

    protected void checkGenerics() throws GenericsException {
        super.checkGenerics();

        if(!implementsInterface.equals("")) {
            checkImplementedInterfaceGenerics();
        }
    }

    private void checkImplementedInterfaceGenerics() throws GenericsException {
        Interface implemented = SymbolTable.getInstance().getInterface(implementsInterface);
        List<String> interfaceGenericTypes = implemented.getGenericTypes();

        implementsTypeParametersArityMatchesInterfaceRealTypeParameterArity(implemented);
        implementsTypeParametersAreDeclaredClassesOrDeclaredTypeParameters();
        createClassToInterfaceGenericTypeMap(interfaceGenericTypes);
    }

    private void createClassToInterfaceGenericTypeMap(List<String> interfaceGenericTypes) {
        for(int i = 0; i < interfaceDeclaredGenericTypes.size(); i++) {
            if(implementedGenericTypesMap.get(interfaceDeclaredGenericTypes.get(i)) == null)
                implementedGenericTypesMap.put(interfaceDeclaredGenericTypes.get(i), new ArrayList<String>());

            implementedGenericTypesMap.get(interfaceDeclaredGenericTypes.get(i)).add(interfaceGenericTypes.get(i));
        }
    }

    private void implementsTypeParametersAreDeclaredClassesOrDeclaredTypeParameters() throws GenericsException {
        for(String implementedGenericType : interfaceDeclaredGenericTypes) {
            if(!(SymbolTable.getInstance().exists(implementedGenericType) || genericTypes.contains(implementedGenericType))) {
                Token genericsToken = new Token(TokenType.id_class, implementedGenericType, getToken().getLineNumber());
                String errorMessage = "The parametric type " + implementedGenericType + " is not valid in that context.";
                throw new GenericsException(genericsToken, errorMessage);
            }
        }
    }

    private void implementsTypeParametersArityMatchesInterfaceRealTypeParameterArity(Interface implemented) throws GenericsException {
        if(interfaceDeclaredGenericTypes.size() != implemented.getGenericTypes().size()) {
            String errorMessage = "When the class " + getName() + " declares implementation of " + implemented.getName()
                    + ", there is a mismatch of number of generic type parameters (" + interfaceDeclaredGenericTypes.size()
                    + " but " + implemented.getName() + " has " + implemented.getGenericTypes().size() + ")";
            throw new GenericsException(getToken(), errorMessage);
        }
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
            for(String g : parentDeclaredGenericTypes) {
                s += prefix + prefix + g + " maps to ";

                if(childToParentGenericTypeMap.get(g) == null)
                    s += "null ";
                else
                    for(String mapped: childToParentGenericTypeMap.get(g))
                        s += mapped + " ";

                s+="\n";
            }
        }

        if(interfaceDeclaredGenericTypes.size() > 0) {
            s += prefix + "INTERFACE DECLARED GENERICS\n";
            for(String g : interfaceDeclaredGenericTypes){
                s += prefix + prefix + g + " maps to ";

                if(implementedGenericTypesMap.get(g) == null) {
                    s += "null ";
                } else {
                    for (String mapped : implementedGenericTypesMap.get(g))
                        s += mapped + " ";
                }

                s+="\n";
            }
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
