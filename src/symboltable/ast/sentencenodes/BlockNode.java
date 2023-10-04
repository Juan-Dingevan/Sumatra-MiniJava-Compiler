package symboltable.ast.sentencenodes;

import java.util.ArrayList;
import java.util.List;

public class BlockNode extends SentenceNode{
    protected List<SentenceNode> sentences;

    public BlockNode() {
        sentences = new ArrayList<>();
    }
}
