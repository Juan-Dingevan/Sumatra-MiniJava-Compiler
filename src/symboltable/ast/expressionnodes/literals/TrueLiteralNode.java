package symboltable.ast.expressionnodes.literals;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;

public class TrueLiteralNode extends BooleanLiteralNode{
    private static final int TRUE = 1;
    public void generate() throws CompilerException {
        CodeGenerator.getInstance().append("PUSH " + TRUE);
    }
}
