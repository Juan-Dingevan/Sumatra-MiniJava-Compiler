package symboltable.ast.expressionnodes.unaryexpressions;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.InvalidTypesInUnaryOperatorException;
import symboltable.ast.expressionnodes.UnaryExpressionNode;
import symboltable.types.*;

public abstract class NumberUnaryExpressionNode extends UnaryExpressionNode {
    public Type check() throws CompilerException {
        Type type = operandExpression.check();
        boolean typeNumber = Type.isNumber(type);

        if(!typeNumber)
            throw new InvalidTypesInUnaryOperatorException(token, new SNumber(), type);

        boolean isFloat = type.equals(new SFloat());

        return isFloat ? new SFloat() : new Int();
    }
}
