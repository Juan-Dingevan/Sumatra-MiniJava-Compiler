package symboltable.ast.sentencenodes.defaultmethodsblocks;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;

public class PrintStringBlockNode extends DefaultMethodBlockNode {
    @Override
    public void generate() throws CompilerException {
        String c1 = " # We put the parameter i at the top of the stack (offset is hard-coded)";
        CodeGenerator.getInstance().append("LOAD 3" + c1);

        String c2 = " # We load the 'fake attribute' through the this reference and the offset (hardcoded)";
        CodeGenerator.getInstance().append("LOADREF 1"+c2);

        String c3 = " # We print the string we just loaded";
        CodeGenerator.getInstance().append("SPRINT" + c3);
    }
}
