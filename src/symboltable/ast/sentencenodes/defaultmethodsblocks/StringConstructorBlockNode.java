package symboltable.ast.sentencenodes.defaultmethodsblocks;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;

public class StringConstructorBlockNode extends DefaultMethodBlockNode{

    @Override
    public void generate() throws CompilerException {
        CodeGenerator.getInstance().append("LOAD 3");

        String c1 = " # We put the empty String tag at the top of the stack";
        CodeGenerator.getInstance().append("PUSH " + CodeGenerator.EMPTY_STRING_TAG + c1);

        String cStore = " # We link the 'fake attribute' to the empty string tag (attribute is hardcoded)";
        CodeGenerator.getInstance().append("STOREREF 1" + cStore);
    }
}
