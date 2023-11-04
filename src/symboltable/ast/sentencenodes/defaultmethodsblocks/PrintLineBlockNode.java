package symboltable.ast.sentencenodes.defaultmethodsblocks;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import symboltable.ast.sentencenodes.BlockNode;

public class PrintLineBlockNode extends DefaultMethodBlockNode {
    @Override
    public void generate() throws CompilerException {
        String c = " # We print the carriage return";
        CodeGenerator.getInstance().append("PRNLN" + c);
    }
}
