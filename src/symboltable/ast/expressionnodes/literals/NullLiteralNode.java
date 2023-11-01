package symboltable.ast.expressionnodes.literals;

import codegenerator.CodeGenerator;
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

    public void generate() throws CompilerException {
        CodeGenerator.getInstance().append("PUSH 0");
    }
}
