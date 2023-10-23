package symboltable.ast.sentencenodes;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.ExpressionInVoidOrConstructorReturn;
import exceptions.semantical.sentence.NoExpressionInTypedMethodReturnException;
import exceptions.semantical.sentence.TypesDontConformException;
import symboltable.ast.expressionnodes.ExpressionNode;
import symboltable.ast.expressionnodes.accesses.VariableAccessNode;
import symboltable.symbols.members.Method;
import symboltable.symbols.members.Unit;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import symboltable.types.Void;

public class ReturnNode extends SentenceNode{
    public static int classID = 0;
    private final int id = classID;
    protected ExpressionNode expression;

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
