package symboltable.ast.sentencenodes;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.ExpressionInVoidOrConstructorReturn;
import exceptions.semantical.sentence.NoExpressionInTypedMethodReturnException;
import exceptions.semantical.sentence.TypesDontConformException;
import symboltable.ast.expressionnodes.ExpressionNode;
import symboltable.symbols.members.Method;
import symboltable.symbols.members.Unit;
import symboltable.types.Type;
import symboltable.types.Void;

public class ReturnNode extends SentenceNode{
    public static int classID = 0;
    private final int id = classID;
    protected ExpressionNode expression;
    protected Unit contextUnit;

    public ExpressionNode getExpression() {
        return expression;
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    public void setContextUnit(Unit contextUnit) {
        this.contextUnit = contextUnit;
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

        if(!Type.typesConform(gotType, expectedType))
            throw new TypesDontConformException(token, expectedType, gotType);
    }
}
