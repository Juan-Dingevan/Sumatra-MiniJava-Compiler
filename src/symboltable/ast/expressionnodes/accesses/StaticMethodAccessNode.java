package symboltable.ast.expressionnodes.accesses;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import exceptions.semantical.declaration.GenericsException;
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
import symboltable.symbols.members.Parameter;
import symboltable.table.SymbolTable;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import token.Token;
import utility.ActualArgumentsHandler;

import java.util.List;

public class StaticMethodAccessNode extends AccessNode {
    protected Token classToken;
    protected List<ExpressionNode> actualArguments;
    protected Method method;

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

        method = callerClass.getMethod(methodName);
        if(method == null)
            throw new UnresolvedNameException(token, callerClass.getToken());

        Privacy privacy = method.getPrivacy();
        boolean accessedFromDeclaringClass = contextClass == method.getMemberOf();
        boolean isPrivate = privacy != Privacy.publicS;

        if(isPrivate && !accessedFromDeclaringClass) {
            throw new PrivateMemberAccessException(token);
        }

        boolean isStatic = method.isStatic();
        if(!isStatic)
            throw new InvalidDynamicAccessException(token, callerClass.getToken());

        for(Parameter formalArgument : method.getParameters()) {
            Type type = formalArgument.getType();

            if(Type.isReferenceType(type)) {
                ReferenceType referenceType = (ReferenceType) type;
                String reference = referenceType.getReferenceName();

                if(callerClass.isGenericType(reference)) {
                    String error = "Error in line " + token.getLineNumber() + ": can't reference method with generic return type statically.";
                    throw new GenericsException(token, error);
                }
            }
        }

        ReferenceType callerType = new ReferenceType(callerClass.getName());
        callerType.setGenericTypes(callerClass.getGenericTypes());
        ActualArgumentsHandler.checkActualArguments(method, actualArguments, token, callerType);

        Type returnType = method.getReturnType();

        if(Type.isReferenceType(returnType)) {
            ReferenceType rt = (ReferenceType) returnType;
            String reference = rt.getReferenceName();

            if(callerClass.isGenericType(reference)) {
                String error = "Error in line " + token.getLineNumber() + ": can't reference method with generic return type statically.";
                throw new GenericsException(token, error);
            }
        }

        return returnType;
    }

    public void accessGenerate() throws CompilerException {
        if(!Type.isVoid(method.getReturnType())) {
            String cRet = " # We reserve a memory cell for the method's return value";
            CodeGenerator.getInstance().append("RMEM 1" + cRet);
        }

        for(ExpressionNode argument : actualArguments) {
            argument.generate();
        }

        String tag = method.getTag();
        String cTag = " # We push the static method's tag to the top of the stack";
        CodeGenerator.getInstance().append("PUSH " + tag + cTag);

        String cCall = " # We make the call.";
        CodeGenerator.getInstance().append("CALL" + cCall);
    }

    @Override
    public boolean isValidAsSentence() {
        if(chainingNode != ChainingNode.NO_CHAINING)
            return chainingNode.isCall();
        else
            return true;
    }
}
