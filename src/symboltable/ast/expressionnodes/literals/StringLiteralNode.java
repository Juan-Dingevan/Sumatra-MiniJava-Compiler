package symboltable.ast.expressionnodes.literals;

import exceptions.general.CompilerException;
import symboltable.ast.expressionnodes.LiteralNode;
import symboltable.types.ReferenceType;
import symboltable.types.Type;

public class StringLiteralNode extends LiteralNode {
    @Override
    public Type check() throws CompilerException {
        return new ReferenceType("String");
    }
}
