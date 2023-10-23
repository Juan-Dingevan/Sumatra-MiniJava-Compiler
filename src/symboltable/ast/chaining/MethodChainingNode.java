package symboltable.ast.chaining;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.*;
import symboltable.ast.expressionnodes.ExpressionNode;
import symboltable.privacy.Privacy;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.members.Method;
import symboltable.table.SymbolTable;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import token.Token;
import utility.ActualArgumentsHandler;

import java.util.List;
public class MethodChainingNode extends ChainingNode{
    protected List<ExpressionNode> actualArguments;

    public void setActualArguments(List<ExpressionNode> actualArguments) {
        this.actualArguments = actualArguments;
    }

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

        ConcreteClass referencedClass = SymbolTable.getInstance().getClass(className);
        Method method = referencedClass.getMethod(methodName);

        if(method == null)
            throw new UnresolvedNameException(token, referencedClass.getToken());

        Privacy privacy = method.getPrivacy();
        boolean accessedFromDeclaringClass = contextClass == method.getMemberOf();
        boolean isPrivate = privacy != Privacy.publicS;

        if(isPrivate && !accessedFromDeclaringClass) {
            throw new PrivateMemberAccessException(token);
        }

        ActualArgumentsHandler.checkActualArguments(method, actualArguments, token, rt);

        Type returnType = method.getReturnType();

        if(Type.isReferenceType(returnType)){
            ReferenceType possibleReturnReferenceType = (ReferenceType) returnType;
            String reference = possibleReturnReferenceType.getReferenceName();

            if(referencedClass.isGenericType(reference)) {
                List<String> genericDeclaration = referencedClass.getGenericTypes();
                List<String> genericInstantiation = rt.getGenericTypes();

                int index = genericDeclaration.indexOf(reference);

                String realReference = genericInstantiation.get(index);
                ReferenceType realReturnType = new ReferenceType(realReference);

                returnType = realReturnType;
            }
        }

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
