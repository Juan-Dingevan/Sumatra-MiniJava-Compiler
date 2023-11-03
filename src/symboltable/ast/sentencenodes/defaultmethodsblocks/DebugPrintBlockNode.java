package symboltable.ast.sentencenodes.defaultmethodsblocks;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;

public class DebugPrintBlockNode extends DefaultMethodBlockNode {
    @Override
    public void generate() throws CompilerException {
        String c1 = " # We put the parameter i at the top of the stack (offset is hard-coded)";
        CodeGenerator.getInstance().append("LOAD 3" + c1);

        String c2 = " # We print the integer we just loaded";
        CodeGenerator.getInstance().append("IPRINT" + c2);

        String c3 = " # We print the carriage return";
        CodeGenerator.getInstance().append("PRNLN" + c3);
    }
}
