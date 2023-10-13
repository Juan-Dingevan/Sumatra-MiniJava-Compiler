package symboltable.ast.expressionnodes.binaryexpressions.others;

import exceptions.general.CompilerException;
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

        //TODO finish implementing this

        return lhsType;
    }
}
