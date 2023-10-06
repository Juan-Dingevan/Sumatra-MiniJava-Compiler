package symboltable.ast.sentencenodes;

import java.util.ArrayList;
import java.util.List;

public class BlockNode extends SentenceNode{
    private static int classID = 0;
    private final int id;
    protected List<SentenceNode> sentences;

    public BlockNode() {
        sentences = new ArrayList<>();
        id = classID;
        classID++;
    }

    public String toString() {
        return "BlockNode{" + id + "} " + token;
    }
}
