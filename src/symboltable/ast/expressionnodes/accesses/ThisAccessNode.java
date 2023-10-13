package symboltable.ast.expressionnodes.accesses;

import exceptions.general.CompilerException;
import symboltable.ast.expressionnodes.AccessNode;
import symboltable.types.ReferenceType;
import symboltable.types.Type;

public class ThisAccessNode extends AccessNode {
    @Override
    protected Type accessCheck() throws CompilerException {
        String name = contextClass.getName();
        ReferenceType rt = new ReferenceType(name);

        return rt;
    }
}
