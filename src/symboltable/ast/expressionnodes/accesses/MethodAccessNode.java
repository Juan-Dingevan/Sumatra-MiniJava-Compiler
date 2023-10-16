package symboltable.ast.expressionnodes.accesses;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.PrivateMemberAccessException;
import exceptions.semantical.sentence.UnresolvedNameException;
import symboltable.ast.chaining.ChainingNode;
import symboltable.ast.expressionnodes.AccessNode;
import symboltable.privacy.Privacy;
import symboltable.symbols.members.Method;
import symboltable.types.Type;

public class MethodAccessNode extends AccessNode {
    @Override
    protected Type accessCheck() throws CompilerException {
        String methodName = token.getLexeme();

        Method method = contextClass.getMethod(methodName);

        if(method == null)
            throw new UnresolvedNameException(token, contextClass.getToken());

        /*
        Privacy privacy = method.getPrivacy();

        if(privacy != Privacy.publicS)
            throw new PrivateMemberAccessException(token);
        */

        Type returnType = method.getReturnType();

        return returnType;
    }

    @Override
    public boolean isValidAsSentence() {
        if(chainingNode != ChainingNode.NO_CHAINING)
            return chainingNode.isCall();
        else
            return true;
    }
}
