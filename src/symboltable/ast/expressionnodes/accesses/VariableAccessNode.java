package symboltable.ast.expressionnodes.accesses;

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

        Privacy privacy = v.getPrivacy();

        boolean accessedFromDeclaringClass = contextClass == v.getMemberOf();
        boolean isPrivate = privacy != Privacy.publicS;

        if(isPrivate && !accessedFromDeclaringClass) {
            throw new PrivateMemberAccessException(token);
        }

        boolean staticContextUnit = contextUnit.isStatic();
        boolean staticReferencedVar = v.isStatic();

        if(staticContextUnit && !staticReferencedVar)
            throw new DynamicUsageInStaticContextException(token);

        int declarationLine = v.getToken().getLineNumber();
        int usageLine = token.getLineNumber();

        if(variableUsedBeforeDeclaration(declarationLine, usageLine))
            throw new UnresolvedNameException(token, contextClass.getToken());

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

        Variable v = contextUnit.getAST().getLocalVariable(name);
        if(v != null)
            return v;

        Attribute a = contextClass.getAttribute(name);
        if(a != null)
            return a;

        throw new UnresolvedNameException(token, contextClass.getToken());
    }
}
