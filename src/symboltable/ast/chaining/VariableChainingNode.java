package symboltable.ast.chaining;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.DynamicUsageInStaticContextException;
import exceptions.semantical.sentence.PrimitiveTypeHasChainingException;
import exceptions.semantical.sentence.PrivateMemberAccessException;
import exceptions.semantical.sentence.UnresolvedNameException;
import symboltable.privacy.Privacy;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.members.Attribute;
import symboltable.table.SymbolTable;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import token.Token;
import token.TokenType;

public class VariableChainingNode extends ChainingNode{
    @Override
    protected boolean selfCanBeAssigned() {
        return true;
    }

    @Override
    protected Type checkSelf(Type callerType, Token callerToken) throws CompilerException {
        if(!Type.isReferenceType(callerType))
            throw new PrimitiveTypeHasChainingException(token, callerType);

        ReferenceType rt = (ReferenceType) callerType;

        String className = rt.getReferenceName();
        String variableName = token.getLexeme();

        ConcreteClass referencedClass = SymbolTable.getInstance().getClass(className);
        Attribute attribute = referencedClass.getAttribute(variableName);

        if(attribute == null)
            throw new UnresolvedNameException(token, referencedClass.getToken());

        Privacy privacy = attribute.getPrivacy();

        boolean accessedFromDeclaringClass = contextClass == attribute.getMemberOf();
        boolean isPrivate = privacy != Privacy.publicS;

        if(isPrivate && !accessedFromDeclaringClass) {
            throw new PrivateMemberAccessException(token);
        }

        Type type = attribute.getType();

        return type;
    }

    @Override
    public boolean isCall() {
        if(hasChaining())
            return chainingNode.isCall();
        else
            return true;
    }


}
