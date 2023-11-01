package symboltable.ast.expressionnodes.literals;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import symboltable.ast.expressionnodes.LiteralNode;
import symboltable.types.Int;
import symboltable.types.Type;

public class IntLiteralNode extends LiteralNode {
    @Override
    public Type check()throws CompilerException {
        return new Int();
    }

    public void generate() throws CompilerException {
        CodeGenerator.getInstance().append("PUSH " + token.getLexeme());
    }
}
