package symboltable.symbols.classes;

import exceptions.general.CompilerException;
import exceptions.semantical.*;
import symboltable.symbols.members.Attribute;
import symboltable.symbols.members.Constructor;
import symboltable.symbols.members.Method;
import symboltable.table.SymbolTable;
import token.Token;
import utility.StringUtilities;

import java.util.HashMap;

import static token.TokenConstants.OBJECT_TOKEN;

public class ConcreteClass extends Class {
    private static int classID = 0;
    private static final int LEVEL = 1;

    protected int instanceID;
    protected HashMap<String, Attribute> attributes;
    protected Constructor constructor;
    protected String implementsInterface;

    public static void resetID() {
        classID = 0;
    }

    public ConcreteClass(Token t) {
        super(t);

        attributes = new HashMap<>();

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

    public void checkDeclaration() throws CompilerException{
        for(Attribute a : attributes.values())
            a.checkDeclaration();

        for(Method m : methods.values())
            m.checkDeclaration();

        if(constructor != null)
            constructor.checkDeclaration();
    }

    @Override
    public void consolidate() throws CompilerException {
        if(token == OBJECT_TOKEN)
            return;

        ConcreteClass parent = SymbolTable.getInstance().getClass(inheritsFrom);
        parent.consolidate();

        for(Attribute parentAttribute : parent.getAttributes())
            if(!attributeExists(parentAttribute)) {
                addAttribute(parentAttribute);
            } else {
                Token rewrittenToken = attributes.get(parentAttribute.getName()).getToken();
                Token originalToken = parentAttribute.getToken();
                throw new OverwrittenAttributeException(rewrittenToken, originalToken);
            }

        for(Method parentMethod : parent.getMethods()) {
            if(!methodExists(parentMethod)) {
                addMethod(parentMethod);
            } else {
                Method childMethod = getMethod(parentMethod.getName());
                if(!childMethod.hasSameSignature(parentMethod))
                    throw new IncorrectlyOverwrittenMethodException(childMethod.getToken(), getToken());
            }
        }

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

    public Iterable<Attribute> getAttributes() {
        return attributes.values();
    }

    public String toString() {
        String name = this.getName();
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "CLASS{" + instanceID + "}: " + name + " EXTENDS: " + inheritsFrom + " IMPLEMENTS: " + implementsInterface + "\n";

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
