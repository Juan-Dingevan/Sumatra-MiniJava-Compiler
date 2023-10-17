package symboltable.ast.expressionnodes.accesses;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.InvalidDynamicAccessException;
import exceptions.semantical.sentence.PrivateMemberAccessException;
import exceptions.semantical.sentence.UndeclaredClassException;
import exceptions.semantical.sentence.UnresolvedNameException;
import symboltable.ast.chaining.ChainingNode;
import symboltable.ast.expressionnodes.AccessNode;
import symboltable.ast.expressionnodes.ExpressionNode;
import symboltable.privacy.Privacy;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.members.Method;
import symboltable.table.SymbolTable;
import symboltable.types.Type;
import token.Token;
import utility.ActualArgumentsHandler;

import java.util.List;

public class StaticMethodAccessNode extends AccessNode {
    protected Token classToken;
    protected List<ExpressionNode> actualArguments;

    public Token getClassToken() {
        return classToken;
    }

    public void setClassToken(Token classToken) {
        this.classToken = classToken;
    }

    public void setActualArguments(List<ExpressionNode> actualArguments) {
        this.actualArguments = actualArguments;
    }

    @Override
    protected Type accessCheck() throws CompilerException {
        String methodName = token.getLexeme();
        String className  = classToken.getLexeme();

        ConcreteClass callerClass = SymbolTable.getInstance().getClass(className);
        if(callerClass == null)
            throw new UndeclaredClassException(classToken);

        Method method = callerClass.getMethod(methodName);
        if(method == null)
            throw new UnresolvedNameException(token, callerClass.getToken());

        Privacy privacy = method.getPrivacy();
        if(privacy != Privacy.publicS)
            throw new PrivateMemberAccessException(token);

        boolean isStatic = method.isStatic();
        if(!isStatic)
            throw new InvalidDynamicAccessException(token, callerClass.getToken());

        ActualArgumentsHandler.checkActualArguments(method, actualArguments, token);

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
