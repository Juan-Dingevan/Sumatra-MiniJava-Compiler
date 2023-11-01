package symboltable.ast.expressionnodes.unaryexpressions.bool;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import symboltable.ast.expressionnodes.unaryexpressions.BooleanUnaryExpressionNode;

public class NotExpressionNode extends BooleanUnaryExpressionNode {
    public void generate() throws CompilerException {
        operandExpression.generate();
        CodeGenerator.getInstance().append("NOT");
    }
}
