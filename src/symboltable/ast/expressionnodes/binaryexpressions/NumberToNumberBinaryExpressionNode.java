package symboltable.ast.expressionnodes.binaryexpressions;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.InvalidTypesInBinaryOperatorException;
import symboltable.ast.expressionnodes.BinaryExpressionNode;
import symboltable.types.*;
import utility.Pair;

public abstract class NumberToNumberBinaryExpressionNode extends BinaryExpressionNode {
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

        boolean atLeastOneFloat = lhsType.equals(new SFloat()) || rhsType.equals(new SFloat());
        Type returnType = atLeastOneFloat ? new SFloat() : new Int();

        return returnType;
    }
}
