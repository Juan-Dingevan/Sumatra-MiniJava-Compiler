package symboltable.ast.expressionnodes.unaryexpressions.num;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import symboltable.ast.expressionnodes.unaryexpressions.NumberUnaryExpressionNode;

public class PlusExpressionNode extends NumberUnaryExpressionNode {
    public void generate() throws CompilerException {
        operandExpression.generate();
        //The + unary operator doesnt do anything
    }
}
