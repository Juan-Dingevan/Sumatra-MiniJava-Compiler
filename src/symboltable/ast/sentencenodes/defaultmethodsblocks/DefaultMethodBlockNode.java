package symboltable.ast.sentencenodes.defaultmethodsblocks;

import symboltable.ast.sentencenodes.BlockNode;
import symboltable.table.DefaultClassesSetUpHandler;

public abstract class DefaultMethodBlockNode extends BlockNode {
    public DefaultMethodBlockNode() {
        super();
        parentBlock = NULL_PARENT;
    }
}
