package symboltable.ast.sentencenodes;

public class CallNode extends SentenceNode{
    public static int classID = 0;
    private final int id = classID;
    @Override
    protected int getID() {
        return id;
    }
}
