package symboltable.ast.expressionnodes.accesses;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import exceptions.semantical.sentence.DynamicUsageInStaticContextException;
import exceptions.semantical.sentence.PrivateMemberAccessException;
import exceptions.semantical.sentence.UnresolvedNameException;
import symboltable.ast.chaining.ChainingNode;
import symboltable.ast.expressionnodes.AccessNode;
import symboltable.ast.expressionnodes.ExpressionNode;
import symboltable.privacy.Privacy;
import symboltable.symbols.members.Method;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import utility.ActualArgumentsHandler;

import java.util.List;

public class MethodAccessNode extends AccessNode {
    protected List<ExpressionNode> actualArguments;
    protected Method method;
    @Override
    protected Type accessCheck() throws CompilerException {
        String methodName = token.getLexeme();

        method = contextClass.getMethod(methodName);

        if(method == null)
            throw new UnresolvedNameException(token, contextClass.getToken());

        ReferenceType callerType = new ReferenceType(contextClass.getName());
        callerType.setGenericTypes(contextClass.getGenericTypes());

        ActualArgumentsHandler.checkActualArguments(method, actualArguments, token, callerType);

        Privacy privacy = method.getPrivacy();

        boolean accessedFromDeclaringClass = contextClass == method.getMemberOf();
        boolean isPrivate = privacy != Privacy.publicS;

        if(isPrivate && !accessedFromDeclaringClass) {
            throw new PrivateMemberAccessException(token);
        }

        boolean staticContextUnit = contextUnit.isStatic();
        boolean staticReferencedMethod = method.isStatic();

        if(staticContextUnit && !staticReferencedMethod)
            throw new DynamicUsageInStaticContextException(token);

        Type returnType = method.getReturnType();

        if(Type.isReferenceType(returnType)) {
            ReferenceType referenceReturnType = (ReferenceType) returnType;
            String reference = referenceReturnType.getReferenceName();

            if(contextClass.isGenericallyInstantiated(reference)) {
                String genericInstantiation = contextClass.instantiateGenericType(reference);
                returnType = new ReferenceType(genericInstantiation);
            }
        }

        return returnType;
    }

    public void setActualArguments(List<ExpressionNode> actualArguments) {
        this.actualArguments = actualArguments;
    }

    public void generate() throws CompilerException {
        //we partially build the called-method's RA
        //we already have the called-method's reference in method
        if(method.isStatic()) {
            generateStaticCall();
        } else {
            generateDynamicCall();
        }

        if(hasChaining())
            chainingNode.generate();
    }

    protected void generateDynamicCall() throws CompilerException {
        String cThis = " # We put a reference to 'this' at the top of the stack";
        CodeGenerator.getInstance().append("LOAD 3" + cThis);

        String cSwap = " # We swap to keep the 'this' reference at the top of the stack";

        if(!Type.isVoid(method.getReturnType())) {
            String cRet = " # We reserve a memory cell for the method's return value";
            CodeGenerator.getInstance().append("RMEM 1" + cRet);

            CodeGenerator.getInstance().append("SWAP " + cSwap);
        }

        for(ExpressionNode argument : actualArguments) {
            argument.generate();
            CodeGenerator.getInstance().append("SWAP " + cSwap);
        }

        String cDup = " # We duplicate the 'this' reference so we don't lose it when it's used by LOADREF";
        CodeGenerator.getInstance().append("DUP" + cDup);

        String cLoad1 = " # We load the VTable (VTable offset is always 0)";
        CodeGenerator.getInstance().append("LOADREF 0" + cLoad1);

        String cLoad2 = " # We load the method's address through the VTable.";
        CodeGenerator.getInstance().append("LOADREF " + method.getOffset() + cLoad2);

        String cCall = " # We make the call.";
        CodeGenerator.getInstance().append("CALL" + cCall);
    }

    protected void generateStaticCall() throws CompilerException {
        //Note: this is a DYNAMIC ACCESS to a STATIC METHOD.
        //It's different from a Dynamic-Dynamic one because we don't add
        // a "this" reference to the RA we build.

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
