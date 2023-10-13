package symboltable.ast.expressionnodes.binaryexpressions;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.InvalidTypesInBinaryOperatorException;
import symboltable.ast.expressionnodes.BinaryExpressionNode;
import symboltable.types.*;
import utility.Pair;

public abstract class OthersToBooleanBinaryExpressionNode extends BinaryExpressionNode {
    public Type check() throws CompilerException {
        Type lhsType = lhs.check();
        Type rhsType = rhs.check();

        //TODO implementar esto bien!
        if(false) {
            Pair<Type, Type> e = new Pair<>(new SNumber(), new SNumber());
            Pair<Type, Type> g = new Pair<>(lhsType, rhsType);
            throw new InvalidTypesInBinaryOperatorException(token, e, g);
        }

        return new SBoolean();
    }
}
