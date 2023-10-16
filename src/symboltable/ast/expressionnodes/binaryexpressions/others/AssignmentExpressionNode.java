package symboltable.ast.expressionnodes.binaryexpressions.others;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.NonAssignableExpressionException;
import exceptions.semantical.sentence.TypesDontConformException;
import symboltable.ast.expressionnodes.BinaryExpressionNode;
import symboltable.types.Type;

public class AssignmentExpressionNode extends BinaryExpressionNode {
    @Override
    public boolean isAssignment() {
        return true;
    }

    @Override
    public Type check() throws CompilerException {
        Type lhsType = lhs.check();
        Type rhsType = rhs.check();

        if(!lhs.canBeAssigned())
            throw new NonAssignableExpressionException(token);

        if(!Type.typesConform(rhsType, lhsType))
            throw new TypesDontConformException(token, lhsType, rhsType);

        return lhsType;
    }
}
