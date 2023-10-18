package symboltable.ast.expressionnodes.accesses;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.PrivateMemberAccessException;
import exceptions.semantical.sentence.UnresolvedNameException;
import symboltable.ast.chaining.ChainingNode;
import symboltable.ast.expressionnodes.AccessNode;
import symboltable.ast.expressionnodes.ExpressionNode;
import symboltable.privacy.Privacy;
import symboltable.symbols.members.Method;
import symboltable.types.Type;
import utility.ActualArgumentsHandler;

import java.util.List;

public class MethodAccessNode extends AccessNode {
    protected List<ExpressionNode> actualArguments;
    @Override
    protected Type accessCheck() throws CompilerException {
        String methodName = token.getLexeme();

        Method method = contextClass.getMethod(methodName);

        if(method == null)
            throw new UnresolvedNameException(token, contextClass.getToken());

        ActualArgumentsHandler.checkActualArguments(method, actualArguments, token);

        Privacy privacy = method.getPrivacy();

        boolean accessedFromDeclaringClass = contextClass == method.getMemberOf();
        boolean isPrivate = privacy != Privacy.publicS;

        if(isPrivate && !accessedFromDeclaringClass) {
            throw new PrivateMemberAccessException(token);
        }

        Type returnType = method.getReturnType();

        return returnType;
    }

    public void setActualArguments(List<ExpressionNode> actualArguments) {
        this.actualArguments = actualArguments;
    }

    @Override
    public boolean isValidAsSentence() {
        if(chainingNode != ChainingNode.NO_CHAINING)
            return chainingNode.isCall();
        else
            return true;
    }
}
