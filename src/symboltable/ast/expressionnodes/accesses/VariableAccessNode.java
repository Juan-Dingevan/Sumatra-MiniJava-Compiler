package symboltable.ast.expressionnodes.accesses;

import com.sun.security.jgss.GSSUtil;
import exceptions.general.CompilerException;
import exceptions.semantical.sentence.PrivateMemberAccessException;
import exceptions.semantical.sentence.UnresolvedNameException;
import symboltable.ast.expressionnodes.AccessNode;
import symboltable.privacy.Privacy;
import symboltable.symbols.members.Attribute;
import symboltable.symbols.members.Parameter;
import symboltable.symbols.members.Unit;
import symboltable.symbols.members.Variable;
import symboltable.types.Type;

public class VariableAccessNode extends AccessNode {
    protected Unit contextUnit;
    protected Variable referencedVariable;

    public VariableAccessNode() {
        super();
        referencedVariable = null;
    }

    public Unit getContextUnit() {
        return contextUnit;
    }

    public void setContextUnit(Unit contextUnit) {
        this.contextUnit = contextUnit;
    }

    @Override
    protected Type accessCheck() throws CompilerException {
        //TODO: chequear que si comparte nombre con parametro no se pueda definir
        String name = token.getLexeme();
        Variable v = resolveName(name);

        Privacy privacy = v.getPrivacy();

        if(privacy != Privacy.publicS)
            throw new PrivateMemberAccessException(token);

        int declarationLine = v.getToken().getLineNumber();
        int usageLine = token.getLineNumber();

        if(variableUsedBeforeDeclaration(declarationLine, usageLine))
            throw new UnresolvedNameException(token, contextClass.getToken());

        Type type = v.getType();
        referencedVariable = v;

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
