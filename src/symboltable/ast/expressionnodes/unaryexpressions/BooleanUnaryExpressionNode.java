package symboltable.ast.expressionnodes.unaryexpressions;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.InvalidTypesInBinaryOperatorException;
import exceptions.semantical.sentence.InvalidTypesInUnaryOperatorException;
import symboltable.ast.expressionnodes.UnaryExpressionNode;
import symboltable.types.*;
import utility.Pair;

public abstract class BooleanUnaryExpressionNode extends UnaryExpressionNode {
    public Type check() throws CompilerException {
        Type type = operandExpression.check();
        boolean typeBoolean = type.equals(new SBoolean());

        if(!typeBoolean)
            throw new InvalidTypesInUnaryOperatorException(token, new SBoolean(), type);

        return new SBoolean();
    }
}
