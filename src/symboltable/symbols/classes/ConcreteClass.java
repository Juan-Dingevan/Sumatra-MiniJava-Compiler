package symboltable.symbols.classes;

import exceptions.general.CompilerException;
import exceptions.semantical.AttributeAlreadyExistsException;
import exceptions.semantical.ClassAlreadyHasConstructorException;
import symboltable.symbols.members.Attribute;
import symboltable.symbols.members.Constructor;
import symboltable.symbols.members.Method;
import token.Token;
import utility.StringUtilities;

import java.util.HashMap;

public class ConcreteClass extends Class {
    private static int classID = 0;
    private static final int LEVEL = 1;

    protected int instanceID;
    protected HashMap<String, Attribute> attributes;
    protected Constructor constructor;
    protected String implementsInterface;

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

        constructor.checkDeclaration();
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
            s += m.toString() + "\n";

        s += prefix + "CONSTRUCTOR:\n";
        s += (constructor == null ? "" : constructor.toString());

        return s;
    }
}
