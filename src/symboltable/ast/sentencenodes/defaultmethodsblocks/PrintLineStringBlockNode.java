package symboltable.ast.sentencenodes.defaultmethodsblocks;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import symboltable.ast.sentencenodes.BlockNode;

public class PrintLineStringBlockNode extends DefaultMethodBlockNode {
    @Override
    public void generate() throws CompilerException {
        String c1 = " # We put the parameter i at the top of the stack (offset is hard-coded)";
        CodeGenerator.getInstance().append("LOAD 3" + c1);

        String c2 = " # We print the String we just loaded";
        CodeGenerator.getInstance().append("SPRINT" + c2);

        String c3 = " # We print the carriage return";
        CodeGenerator.getInstance().append("PRNLN" + c3);
    }
}
