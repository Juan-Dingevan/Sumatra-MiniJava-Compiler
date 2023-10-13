package symboltable.ast.expressionnodes.literals;

import exceptions.general.CompilerException;
import symboltable.ast.expressionnodes.LiteralNode;
import symboltable.types.Int;
import symboltable.types.Type;

public class IntLiteralNode extends LiteralNode {
    @Override
    public Type check()throws CompilerException {
        return new Int();
    }
}
