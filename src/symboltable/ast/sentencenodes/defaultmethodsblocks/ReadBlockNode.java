package symboltable.ast.sentencenodes.defaultmethodsblocks;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import symboltable.ast.sentencenodes.BlockNode;

public class ReadBlockNode extends DefaultMethodBlockNode {
    @Override
    public void generate() throws CompilerException {
        String c1 = " # We read the next integer value on the input stream.";
        CodeGenerator.getInstance().append("READ" + c1);

        String c2 = " # We push 48 because the integer 0 is represented by the char number 48 in the ASCII table";
        CodeGenerator.getInstance().append("PUSH 48" + c2);

        String c3 = " # We subtract that 'ASCII Offset' to get the real number";
        CodeGenerator.getInstance().append("SUB" + c3);

        String c4 = " # We save the return value in the space reserved for it (offset is hard-coded)";
        CodeGenerator.getInstance().append("STORE 3" + c4);
    }
}
