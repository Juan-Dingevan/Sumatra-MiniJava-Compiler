package symboltable.ast.sentencenodes;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import exceptions.semantical.sentence.ExpressionInVoidOrConstructorReturn;
import exceptions.semantical.sentence.NoExpressionInTypedMethodReturnException;
import exceptions.semantical.sentence.TypesDontConformException;
import symboltable.ast.expressionnodes.ExpressionNode;
import symboltable.symbols.members.Member;
import symboltable.symbols.members.Method;
import symboltable.symbols.members.Unit;
import symboltable.types.ReferenceType;
import symboltable.types.Type;

public class ReturnNode extends SentenceNode{
    public static int classID = 0;
    private final int id;
    protected ExpressionNode expression;

    public ReturnNode() {
        super();
        id = classID;
        classID++;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    @Override
    protected int getID() {
        return id;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());

        sb.append("\n");
        sb.append(tabs());
        sb.append("expression: \n");

        if(expression != null) {
            expression.stringDepth = stringDepth+1;
            sb.append(expression);
        } else {
            sb.append(tabs());
            sb.append("[null]");
        }

        return sb.toString();
    }

    @Override
    protected void checkSelf() throws CompilerException {
        if (Unit.isConstructor(contextUnit)) {
            checkConstructorOrVoidReturn();
        } else {
            Method m = (Method) contextUnit;
            Type expectedType = m.getReturnType();

            if(Type.isVoid(expectedType))
                checkConstructorOrVoidReturn();
            else
                checkExpressionReturn(expectedType);
        }
    }

    private void checkConstructorOrVoidReturn() throws ExpressionInVoidOrConstructorReturn {
        if(expression != null)
            throw new ExpressionInVoidOrConstructorReturn(token, contextUnit.getToken());
    }

    @Override
    public void generate() throws CompilerException {
        if (Unit.isConstructor(contextUnit)) {
            generateUnitExit();
        } else {
            Method m = (Method) contextUnit;
            Type type = m.getReturnType();

            if(Type.isVoid(type))
                generateUnitExit();
            else
                generateExpressionReturn();
        }
    }

    private void generateExpressionReturn() throws CompilerException {
        int parameterCount = contextUnit.getParameters().size();
        int dynamicBonus = contextUnit.isStatic() ? 0 : 1;

        int returnCellOffset = Member.PARAMETER_MIN_OFFSET + parameterCount + dynamicBonus;

        expression.generate();

        String cReturn = " # We save the return value in the space reserved for it";
        CodeGenerator.getInstance().append("STORE " + returnCellOffset + cReturn);

        generateUnitExit();
    }

    private void generateUnitExit() throws CompilerException {
        int parentBlockLastOffset = parentBlock.getOffset();
        int localVarCount = -parentBlockLastOffset;

        int parameterCount = contextUnit.getParameters().size();
        int dynamicBonus = contextUnit.isStatic() ? 0 : 1;

        int retParameter = parameterCount + dynamicBonus;

        if(localVarCount > 0) {
            String cFree = " # On return: We free the space used by local vars";
            CodeGenerator.getInstance().append("FMEM " + localVarCount + cFree);
        }

        String cStoreFP = " # On return: We point FP to caller's AR";
        CodeGenerator.getInstance().append("STOREFP" + cStoreFP);

        String cRet = " # On return: We free up memory cells equal to number of params [+1 if unit is dynamic]";
        CodeGenerator.getInstance().append("RET " + retParameter + cRet);
    }

    private void checkExpressionReturn(Type expectedType) throws CompilerException {
        if(expression == null)
            throw new NoExpressionInTypedMethodReturnException(token, contextUnit.getToken());

        Type gotType = expression.check();

        if(!Type.typesConformInContext(gotType, expectedType, contextClass)) {
            boolean lhsIsReferenceType = Type.isReferenceType(expectedType);
            boolean rhsIsReferenceType = Type.isReferenceType(gotType);
            boolean bothSidesAreReferenceType = lhsIsReferenceType && rhsIsReferenceType;

            if(bothSidesAreReferenceType) {
                ReferenceType lhsReferenceType = (ReferenceType) expectedType;
                ReferenceType rhsReferenceType = (ReferenceType) gotType;

                boolean rhsDiamondNotation = rhsReferenceType.usesDiamondNotation();
                boolean lhsHasGenericTypes = lhsReferenceType.hasGenericTypes();

                boolean diamondCanBeInferred = rhsDiamondNotation &&
                        lhsHasGenericTypes;

                if(diamondCanBeInferred) {
                    rhsReferenceType.setGenericTypes(lhsReferenceType.getGenericTypes());
                } else {
                    throw new TypesDontConformException(token, expectedType, gotType);
                }
            } else {
                throw new TypesDontConformException(token, expectedType, gotType);
            }
        }
    }

}
