package symboltable.ast.expressionnodes.literals;

import exceptions.general.CompilerException;
import symboltable.ast.expressionnodes.LiteralNode;
import symboltable.types.Char;
import symboltable.types.Type;

public class CharLiteralNode extends LiteralNode {
    @Override
    public Type check() throws CompilerException {
        return new Char();
    }
}
