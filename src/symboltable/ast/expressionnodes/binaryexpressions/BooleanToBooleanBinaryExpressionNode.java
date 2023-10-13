package symboltable.ast.expressionnodes.binaryexpressions;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.InvalidTypesInBinaryOperatorException;
import symboltable.ast.expressionnodes.BinaryExpressionNode;
import symboltable.types.SBoolean;
import symboltable.types.Type;
import utility.Pair;

public abstract class BooleanToBooleanBinaryExpressionNode extends BinaryExpressionNode {
    public Type check() throws CompilerException {
        Type lhsType = lhs.check();
        Type rhsType = rhs.check();

        boolean lhsBoolean =  lhsType.equals(new SBoolean());
        boolean rhsBoolean =  rhsType.equals(new SBoolean());

        if(!lhsBoolean || !rhsBoolean) {
            Pair<Type, Type> e = new Pair<>(new SBoolean(), new SBoolean());
            Pair<Type, Type> g = new Pair<>(lhsType, rhsType);
            throw new InvalidTypesInBinaryOperatorException(token, e, g);
        }

        return new SBoolean();
    }
}
