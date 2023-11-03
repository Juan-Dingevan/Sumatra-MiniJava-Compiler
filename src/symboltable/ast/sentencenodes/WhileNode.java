package symboltable.ast.sentencenodes;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;

public class WhileNode extends ControlStructureNode {
    public static int classID = 0;
    private final int id = classID;
    @Override
    protected int getID() {
        return id;
    }

    @Override
    public void generate() throws CompilerException {
        String tagIn = "while_" + id;
        String tagOut = "out_while_" + id;

        CodeGenerator.getInstance().append(tagIn + ": NOP");
        expression.generate();
        CodeGenerator.getInstance().append("BF " + tagOut);
        implicitBlock.generate();
        CodeGenerator.getInstance().append("JUMP " + tagIn);
        CodeGenerator.getInstance().append(tagOut + ": NOP");

    }
}
