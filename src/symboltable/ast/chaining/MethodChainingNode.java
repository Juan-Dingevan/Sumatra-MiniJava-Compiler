package symboltable.ast.chaining;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.InvalidDynamicAccessException;
import exceptions.semantical.sentence.PrimitiveTypeHasChainingException;
import exceptions.semantical.sentence.PrivateMemberAccessException;
import exceptions.semantical.sentence.UnresolvedNameException;
import symboltable.privacy.Privacy;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.members.Method;
import symboltable.table.SymbolTable;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import token.Token;
import token.TokenType;

public class MethodChainingNode extends ChainingNode{
    @Override
    protected boolean selfCanBeAssigned() {
        return false;
    }

    @Override
    protected Type checkSelf(Type callerType, Token callerToken) throws CompilerException {
        if(!Type.isReferenceType(callerType))
            throw new PrimitiveTypeHasChainingException(token, callerType);

        ReferenceType rt = (ReferenceType) callerType;

        String className = rt.getReferenceName();
        String methodName = token.getLexeme();

        ConcreteClass contextClass = SymbolTable.getInstance().getClass(className);
        Method method = contextClass.getMethod(methodName);

        if(method == null)
            throw new UnresolvedNameException(token, contextClass.getToken());

        Privacy privacy = method.getPrivacy();

        if(privacy != Privacy.publicS)
            throw new PrivateMemberAccessException(token);

        boolean callerIsClassID = callerToken.getTokenType() == TokenType.id_class;
        boolean isStatic = method.isStatic();

        if(callerIsClassID && !isStatic)
            throw new InvalidDynamicAccessException(token, contextClass.getToken());

        Type returnType = method.getReturnType();

        return returnType;
    }

    @Override
    public boolean isCall() {
        if(hasChaining())
            return chainingNode.isCall();
        else
            return true;
    }
}
