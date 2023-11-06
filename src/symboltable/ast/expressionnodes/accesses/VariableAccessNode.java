package symboltable.ast.expressionnodes.accesses;

import codegenerator.CodeGenerator;
import com.sun.security.jgss.GSSUtil;
import exceptions.general.CompilerException;
import exceptions.semantical.sentence.DynamicUsageInStaticContextException;
import exceptions.semantical.sentence.PrivateMemberAccessException;
import exceptions.semantical.sentence.UnresolvedNameException;
import symboltable.ast.expressionnodes.AccessNode;
import symboltable.privacy.Privacy;
import symboltable.symbols.members.Attribute;
import symboltable.symbols.members.Parameter;
import symboltable.symbols.members.Unit;
import symboltable.symbols.members.Variable;
import symboltable.types.ReferenceType;
import symboltable.types.Type;

public class VariableAccessNode extends AccessNode {
    protected Variable referencedVariable;

    public VariableAccessNode() {
        super();
        referencedVariable = null;
    }

    @Override
    protected boolean accessCanBeAssigned() {
        return true;
    }

    public Variable getReferencedVariable() {
        return referencedVariable;
    }

    @Override
    protected Type accessCheck() throws CompilerException {
        String name = token.getLexeme();
        Variable v = resolveName(name);

        boolean isAttribute = Variable.isAttribute(v);

        Privacy privacy = v.getPrivacy();

        boolean accessedFromDeclaringClass = contextClass == v.getMemberOf();
        boolean isPrivate = privacy != Privacy.publicS;

        if(isPrivate && !accessedFromDeclaringClass) {
            throw new PrivateMemberAccessException(token);
        }

        boolean staticContextUnit = contextUnit.isStatic();
        boolean staticReferencedVar = v.isStatic();
        boolean staticityProblems = (staticContextUnit && !staticReferencedVar) && isAttribute;

        if(staticityProblems)
            throw new DynamicUsageInStaticContextException(token);

        int declarationLine = v.getToken().getLineNumber();
        int usageLine = token.getLineNumber();

        boolean usedBeforeDeclaration = variableUsedBeforeDeclaration(declarationLine, usageLine);
        boolean attribute = Variable.isAttribute(v);

        if(!attribute && usedBeforeDeclaration) {
            throw new UnresolvedNameException(token, contextClass.getToken());
        }

        Type type = v.getType();
        referencedVariable = v;

        if(Type.isReferenceType(type)) {
            ReferenceType referenceReturnType = (ReferenceType) type;
            String reference = referenceReturnType.getReferenceName();

            if(contextClass.isGenericallyInstantiated(reference)) {
                String genericInstantiation = contextClass.instantiateGenericType(reference);
                type = new ReferenceType(genericInstantiation);
            }
        }

        return type;
    }

    private boolean variableUsedBeforeDeclaration(int declarationLine, int usageLine) {
        return usageLine < declarationLine;
    }

    protected Variable resolveName(String name) throws CompilerException{
        Parameter p = contextUnit.getParameter(name);
        if(p != null)
            return p;

        Variable v = parentBlock.getLocalVariable(name);
        if(v != null)
            return v;


        Attribute a = contextClass.getAttribute(name);
        if(a != null)
            return a;

        throw new UnresolvedNameException(token, contextClass.getToken());
    }

    @Override
    public void accessGenerate() throws CompilerException {
        if(referencedVariable.isStatic()) {
            generateStaticAccess();
        } else {
            generateDynamicAccess();
        }
    }

    protected void generateStaticAccess() throws CompilerException {
        //TODO: consultar!

        boolean readAccess = !isAssignmentLHS || hasChaining();
        Attribute attribute = (Attribute) referencedVariable;
        String tag = CodeGenerator.getAttributeTag(attribute);

        String cPush = " # We put the static attribute's tag at the top of the stack";
        CodeGenerator.getInstance().append("PUSH " + tag + cPush);

        if(readAccess) {
            String cRead = " # We read the attributes value from the data using the tag";
            CodeGenerator.getInstance().append("LOADREF 0" + cRead);
        } else {
            String cSwap = " # We swap the tag and what we desire to write into the attribute";
            CodeGenerator.getInstance().append("SWAP" + cSwap);

            String cWrite = " # We write into the data through the tag";
            CodeGenerator.getInstance().append("STOREREF 0" + cWrite);
        }
    }

    protected void generateDynamicAccess() throws CompilerException {
        boolean attribute = Variable.isAttribute(referencedVariable);
        boolean readAccess = !isAssignmentLHS || hasChaining();
        int offset = referencedVariable.getOffset();

        if(attribute) {
            String cThis = " # Accessing a dynamic attribute, we put a reference to 'this' at the top of the stack.";
            CodeGenerator.getInstance().append("LOAD 3" + cThis);

            if(readAccess) {
                String cRead = " # We get the variable from the heap through the 'this' reference and its offset";
                CodeGenerator.getInstance().append("LOADREF " + offset + cRead);
            } else {
                String cSwap = " # We swap the 'this' reference and what we desire to write into the attribute";
                CodeGenerator.getInstance().append("SWAP" + cSwap);

                String cWrite = " # We write into the heap through the 'this' reference and the offset";
                CodeGenerator.getInstance().append("STOREREF " + offset + cWrite);
            }
        } else {
            if(readAccess) {
                String cLoad = " # We get the variable from the stack through its offset";
                CodeGenerator.getInstance().append("LOAD " + offset + cLoad);
            } else {
                String cStore = " # We write into the variable in the stack, through its offset";
                CodeGenerator.getInstance().append("STORE " + offset + cStore);
            }
        }
    }
}
