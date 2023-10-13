package symboltable.ast.expressionnodes.binaryexpressions;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.InvalidTypesInBinaryOperatorException;
import symboltable.ast.expressionnodes.BinaryExpressionNode;
import symboltable.types.SBoolean;
import symboltable.types.SNumber;
import symboltable.types.Type;
import utility.Pair;

public abstract class NumberToBooleanBinaryExpressionNode extends BinaryExpressionNode  {
    public Type check() throws CompilerException {
        Type lhsType = lhs.check();
        Type rhsType = rhs.check();

        boolean lhsNumber =  Type.isNumber(lhsType);
        boolean rhsNumber =  Type.isNumber(rhsType);

        if(!lhsNumber || !rhsNumber) {
            Pair<Type, Type> e = new Pair<>(new SNumber(), new SNumber());
            Pair<Type, Type> g = new Pair<>(lhsType, rhsType);
            throw new InvalidTypesInBinaryOperatorException(token, e, g);
        }

        return new SBoolean();
    }
}
