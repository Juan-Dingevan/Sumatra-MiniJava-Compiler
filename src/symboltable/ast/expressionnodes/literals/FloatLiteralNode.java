package symboltable.ast.expressionnodes.literals;

import exceptions.general.CompilerException;
import symboltable.ast.expressionnodes.LiteralNode;
import symboltable.types.SFloat;
import symboltable.types.Type;

public class FloatLiteralNode extends LiteralNode {
    @Override
    public Type check() throws CompilerException {
        return new SFloat();
    }
}
