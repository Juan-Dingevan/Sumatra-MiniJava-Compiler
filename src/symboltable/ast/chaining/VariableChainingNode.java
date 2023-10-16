package symboltable.ast.chaining;

import exceptions.general.CompilerException;
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

        ConcreteClass contextClass = SymbolTable.getInstance().getClass(className);
        Attribute attribute = contextClass.getAttribute(variableName);

        if(attribute == null)
            throw new UnresolvedNameException(token, contextClass.getToken());

        Privacy privacy = attribute.getPrivacy();

        if(privacy != Privacy.publicS)
            throw new PrivateMemberAccessException(token);

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
