package symboltable.ast.chaining;

import codegenerator.CodeGenerator;
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
    protected Method method;

    public void setActualArguments(List<ExpressionNode> actualArguments) {
        this.actualArguments = actualArguments;
    }

    @Override
    protected void selfGenerate() throws CompilerException {
        if(method.isStatic()) {
            generateStaticCall();
        } else {
            generateDynamicCall();
        }
    }
    private void generateStaticCall() throws CompilerException {
        String cPop = " # We consume the 'this' reference from the 'chainee' that's on top of the stack, since we won't use it";
        CodeGenerator.getInstance().append("POP" + cPop);

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

    private void generateDynamicCall() throws CompilerException {
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
        method = referencedClass.getMethod(methodName);

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

            //Cubre casos de forma:
            //B<E> {...}
            //A<X> extends B<X> {...}
            if(referencedClass.isGenericType(reference)) {
                List<String> genericDeclaration = referencedClass.getGenericTypes();
                List<String> genericInstantiation = rt.getGenericTypes();

                int index = genericDeclaration.indexOf(reference);

                if(genericInstantiation.size() > 0) {
                    String realReference = genericInstantiation.get(index);
                    ReferenceType realReturnType = new ReferenceType(realReference);
                    returnType = realReturnType;
                }
            }

            //Cubre casos de forma:
            //B<E> {...}
            //A extends B<String> {...}
            if(referencedClass.isGenericallyInstantiated(reference)) {
                String realReference = referencedClass.instantiateGenericType(reference);
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
