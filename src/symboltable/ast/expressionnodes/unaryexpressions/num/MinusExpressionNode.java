package symboltable.ast.expressionnodes.unaryexpressions.num;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import symboltable.ast.expressionnodes.unaryexpressions.NumberUnaryExpressionNode;

public class MinusExpressionNode extends NumberUnaryExpressionNode {
    public void generate() throws CompilerException {
        operandExpression.generate();
        CodeGenerator.getInstance().append("NEG");
    }
}
