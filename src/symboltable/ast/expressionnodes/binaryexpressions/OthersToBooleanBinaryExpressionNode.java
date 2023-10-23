package symboltable.ast.expressionnodes.binaryexpressions;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.InvalidTypesInBinaryOperatorException;
import exceptions.semantical.sentence.TypesDontConformException;
import symboltable.ast.expressionnodes.BinaryExpressionNode;
import symboltable.types.*;
import utility.Pair;

public abstract class OthersToBooleanBinaryExpressionNode extends BinaryExpressionNode {
    public Type check() throws CompilerException {
        Type lhsType = lhs.check();
        Type rhsType = rhs.check();

        boolean lhsConformsToRHS = Type.isNull(rhsType) || Type.typesConformInContext(lhsType, rhsType, contextClass);
        boolean rhsConformsToLHS = Type.isNull(lhsType) || Type.typesConformInContext(rhsType, lhsType, contextClass);
        boolean typesConform = lhsConformsToRHS || rhsConformsToLHS;

        if(!typesConform)
            throw new TypesDontConformException(token, lhsType, rhsType);

        return new SBoolean();
    }
}
