package symboltable.ast.expressionnodes.literals;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;

public class FalseLiteralNode extends BooleanLiteralNode {
    private static final int FALSE = 0;
    public void generate() throws CompilerException {
        CodeGenerator.getInstance().append("PUSH " + FALSE);
    }
}
