package symboltable.ast.sentencenodes.defaultmethodsblocks;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import symboltable.ast.sentencenodes.BlockNode;

public class PrintLineIntBlockNode extends DefaultMethodBlockNode {
    @Override
    public void generate() throws CompilerException {
        String c1 = " # We put the parameter i at the top of the stack (offset is hard-coded)";
        CodeGenerator.getInstance().append("LOAD 3" + c1);

        String c2 = " # We print the integer we just loaded";
        CodeGenerator.getInstance().append("IPRINT" + c2);

        String c3 = " # We print the carriage return";
        CodeGenerator.getInstance().append("PRNLN" + c3);

        /**String c4 = " # We point FP to caller's AR";
        CodeGenerator.getInstance().append("STOREFP" + c4);

        String c5 = " # We return from printIln, we free 1 (hard-coded) because theres 1 param and no reference to 'this'";
        CodeGenerator.getInstance().append("RET 1" + c5);**/
    }
}
