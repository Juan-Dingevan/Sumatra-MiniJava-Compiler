package symboltable.ast.expressionnodes.literals;

import exceptions.general.CompilerException;
import symboltable.ast.expressionnodes.LiteralNode;
import symboltable.types.SBoolean;
import symboltable.types.Type;

public abstract class BooleanLiteralNode extends LiteralNode {
    public Type check() throws CompilerException {
        return new SBoolean();
    }
}
