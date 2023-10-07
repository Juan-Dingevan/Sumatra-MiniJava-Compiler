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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());

        sb.append("\n");
        sb.append(tabs());
        sb.append("sentence:\n");

        if(sentence != null) {
            sentence.stringDepth = stringDepth + 1;
            sb.append(sentence);
            sb.append("\n");
        } else {
            sb.append("null\n");
        }

        return sb.toString();
    }
}
