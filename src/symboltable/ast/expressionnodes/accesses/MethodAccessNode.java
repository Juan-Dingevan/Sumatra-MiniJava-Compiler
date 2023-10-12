package symboltable.ast.expressionnodes.accesses;

import symboltable.ast.chaining.ChainingNode;
import symboltable.ast.expressionnodes.AccessNode;

public class MethodAccessNode extends AccessNode {
    @Override
    public boolean isValidAsSentence() {
        if(chainingNode != ChainingNode.NO_CHAINING)
            return chainingNode.isCall();
        else
            return true;
    }
}
