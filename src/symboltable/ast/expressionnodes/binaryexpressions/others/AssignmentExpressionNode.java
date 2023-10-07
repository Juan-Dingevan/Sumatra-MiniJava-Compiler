package symboltable.ast.expressionnodes.binaryexpressions.others;

import symboltable.ast.expressionnodes.BinaryExpressionNode;

public class AssignmentExpressionNode extends BinaryExpressionNode {
    @Override
    public boolean isAssignment() {
        return true;
    }
}
