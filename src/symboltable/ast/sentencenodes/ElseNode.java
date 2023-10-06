package symboltable.ast.sentencenodes;

public class ElseNode extends SentenceNode {
    public static final ElseNode NULL_ELSE = new ElseNode();
    protected SentenceNode sentence;

    public static int classID = 0;
    private final int id = classID;
    @Override
    protected int getID() {
        return id;
    }

    public SentenceNode getSentence() {
        return sentence;
    }

    public void setSentence(SentenceNode sentence) {
        this.sentence = sentence;
    }


}
