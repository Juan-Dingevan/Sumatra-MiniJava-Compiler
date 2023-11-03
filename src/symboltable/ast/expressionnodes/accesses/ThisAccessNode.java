package symboltable.ast.expressionnodes.accesses;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import exceptions.semantical.sentence.ThisAccessInStaticMethodException;
import symboltable.ast.expressionnodes.AccessNode;
import symboltable.types.ReferenceType;
import symboltable.types.Type;

public class ThisAccessNode extends AccessNode {
    @Override
    protected Type accessCheck() throws CompilerException {
        if(contextUnit.isStatic())
            throw new ThisAccessInStaticMethodException(token);

        String name = contextClass.getName();
        ReferenceType rt = new ReferenceType(name);

        return rt;
    }

    @Override
    public void accessGenerate() throws CompilerException {
        String c = " # We put a reference to 'this' at the top of the stack";
        CodeGenerator.getInstance().append("LOAD 3" + c);
    }
}
