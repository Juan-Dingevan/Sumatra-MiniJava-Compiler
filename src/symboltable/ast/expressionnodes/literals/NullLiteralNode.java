package symboltable.ast.expressionnodes.literals;

import exceptions.general.CompilerException;
import symboltable.ast.expressionnodes.LiteralNode;
import symboltable.types.NullType;
import symboltable.types.ReferenceType;
import symboltable.types.Type;

public class NullLiteralNode extends LiteralNode {
    @Override
    public Type check() throws CompilerException {
        return new NullType();
    }
}
