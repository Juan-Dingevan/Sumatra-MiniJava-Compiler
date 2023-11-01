package symboltable.symbols.classes;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import exceptions.semantical.SemanticException;
import exceptions.semantical.declaration.*;
import symboltable.symbols.members.Attribute;
import symboltable.symbols.members.Constructor;
import symboltable.symbols.members.Method;
import symboltable.table.SymbolTable;
import token.Token;
import token.TokenType;
import utility.StringUtilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static token.TokenConstants.OBJECT_TOKEN;

public class ConcreteClass extends Class {
    public static int classID = 0;
    private static final int LEVEL = 1;

    protected int instanceID;
    protected HashMap<String, Attribute> attributes;
    protected List<String> interfaceDeclaredGenericTypes;
    protected Constructor constructor;
    protected String implementsInterface;

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

    public Attribute getAttribute(String name) {
        return attributes.get(name);
    }

    protected boolean attributeExists(Attribute a) {
        return attributes.get(a.getName()) != null;
    }

    public boolean constructorExists() {
        return constructor != null;
    }

    public void addAttribute(Attribute a) throws CompilerException{
        if(!attributeExists(a))
            attributes.put(a.getName(), a);
        else
            throw new AttributeAlreadyExistsException(a.getToken());
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor c) throws CompilerException {
        if(!constructorExists()) {
            constructor = c;
            currentUnit = c;
        } else
            throw new ClassAlreadyHasConstructorException(c.getToken());
    }

    @SuppressWarnings("ReassignedVariable")
    protected void checkCircularInheritance() throws SemanticException {
        Class currentClass = this;

        while(!currentClass.getInheritsFrom().equals("")) {
            String parentName = currentClass.getInheritsFrom();

            if(getName().equals(parentName)) {
                throw new CircularInheritanceException(getToken());
            }

            currentClass = SymbolTable.getInstance().getClass(parentName);

            if(currentClass == null)
                throw new UndeclaredExtendsException(getToken(), parentName);
        }

    }

    @Override
    public boolean isDescendantOf(Class possibleAncestor) {
        String possibleAncestorName = possibleAncestor.getName();

        boolean sameClass = getName().equals(possibleAncestorName);
        boolean directImplementation = implementsInterface.equals(possibleAncestorName);

        if (sameClass || directImplementation) {
            return true;
        }

        if(token == OBJECT_TOKEN) {
            return false;
        }

        Class parent = SymbolTable.getInstance().getClass(inheritsFrom);

        return parent.isDescendantOf(possibleAncestor);
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

        checkGenerics();

        Collection<Attribute> nonInheritedAttributes = attributes.values();

        addInheritedAttributes(parent);
        addInheritedMethods(parent);

        checkCorrectImplementationOfInterface();

        if(implementsInterface.equals(""))
            nextMethodOffset = parent.getNextMethodOffset();
        else {
            Interface implementedInterface = SymbolTable.getInstance().getInterface(implementsInterface);
            nextMethodOffset = implementedInterface.getNextMethodOffset();
        }

        for(Method method : getMethods()) {
            boolean isStatic = method.isStatic();
            boolean isRedefined = method.isRedefined();
            boolean isInherited = method.getMemberOf() != this;

            boolean needsOffset = !(isStatic || isRedefined || isInherited);

            if(needsOffset) {
                System.out.println("in " + getName() + " adding offset " + nextMethodOffset + " to met " + method.getName());
                method.setOffset(nextMethodOffset);
                nextMethodOffset++;
            }
        }

        nextAttributeOffset = parent.getNextAttributeOffset();
        for(Attribute attribute : getAttributes()) {
            boolean isStatic = attribute.isStatic();
            boolean isInherited = attribute.getMemberOf() != this;

            boolean needsOffset = !(isStatic || isInherited);

            if(needsOffset) {
                System.out.println("in " + getName() + " adding offset " + nextAttributeOffset + " to met " + attribute.getName());
                attribute.setOffset(nextAttributeOffset);
                nextAttributeOffset++;
            }
        }

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

                    if(implementedMethod.hasSameSignature(interfaceMethod)) {
                        int interfaceMethodOffset = interfaceMethod.getOffset();
                        implementedMethod.setOffset(interfaceMethodOffset);
                        implementedMethod.setRedefined(true);
                    } else {
                        throw new IncorrectlyOverwrittenMethodException(implementedMethod.getToken(), getToken());
                    }

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

                if(childMethod.hasSameSignature(parentMethod)) {
                    int parentMethodOffset = parentMethod.getOffset();
                    childMethod.setOffset(parentMethodOffset);
                    childMethod.setRedefined(true);
                } else {
                    throw new IncorrectlyOverwrittenMethodException(childMethod.getToken(), getToken());
                }
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

    public void generate() throws CompilerException {
        generateVTable();
        generateAttributes();
        generateMethods();
    }

    private void generateAttributes() throws CompilerException {
        List<Attribute> staticAttributes = new ArrayList<>();
        for(Attribute attribute : getAttributes()) {
            if(attribute.isStatic()) {
                staticAttributes.add(attribute);
            }
        }

        if(staticAttributes.size() > 0)
            CodeGenerator.getInstance().append(".DATA");

        for(Attribute attribute : staticAttributes) {
            String tag = CodeGenerator.getAttributeTag(attribute);
            String instruction = tag + " : DW 0"; //We give them an empty initialization
            CodeGenerator.getInstance().append(instruction);
        }

        if(staticAttributes.size() > 0)
            CodeGenerator.getInstance().addBreakLine();
    }

    private void generateMethods() throws CompilerException {
        Collection<Method> methods = getMethods();

        if(methods.size() > 0)
            CodeGenerator.getInstance().append(".CODE");

        for(Method m : getMethods()) {
            boolean declaredInCurrentClass = m.getMemberOf() == this;
            boolean redefined = m.isRedefined();
            if(declaredInCurrentClass || redefined) {
                m.generate();
                CodeGenerator.getInstance().addBreakLine();
            }
        }
    }

    @SuppressWarnings("ReassignedVariable")
    private void generateVTable() throws CompilerException {
        int numberOfMethods = getMethods().size();
        int numberOfDynamicMethods = 0;
        String[] tagsInOrder = new String[numberOfMethods];

        for(Method m : getMethods()) {
            if(!m.isStatic()) {
                int offset = m.getOffset();
                tagsInOrder[offset] = CodeGenerator.getMethodTag(m);
                numberOfDynamicMethods++;
                //O(n) sorting ;)
            }
        }

        if(numberOfDynamicMethods > 0) {
            String tag = CodeGenerator.getVTableTag(this);

            StringBuilder sb = new StringBuilder(tag);
            sb.append(": DW ");

            for(int i = 0; i < tagsInOrder.length; i++) {
                if(tagsInOrder[i] != null) {
                    sb.append(tagsInOrder[i]);
                    sb.append(",");
                }
            }

            int lastIndexOfComma = sb.lastIndexOf(",");
            sb.deleteCharAt(lastIndexOfComma);

            String instruction = sb.toString();

            CodeGenerator.getInstance().append(".DATA");
            CodeGenerator.getInstance().append(instruction);
            CodeGenerator.getInstance().addBreakLine();
        }
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
