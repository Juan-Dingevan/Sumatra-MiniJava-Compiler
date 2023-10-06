package symboltable.ast.sentencenodes;

import java.util.ArrayList;
import java.util.List;

public class BlockNode extends SentenceNode{
    public static final BlockNode NULL_PARENT = new BlockNode();
    public static int classID = 0;
    private final int id;
    protected List<SentenceNode> sentences;

    public BlockNode() {
        sentences = new ArrayList<>();
        id = classID;
        classID++;
    }

    @Override
    protected int getID() {
        return id;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(super.toString());

        if(sentences.size() > 0) {
            sb.append("children:\n");

            for(SentenceNode s : sentences) {
                sb.append("\t");
                sb.append(s == null ? "null" : s.toString());
                sb.append("\n");
            }
        } else {
            sb.append("[empty]\n");
        }

        return sb.toString();
    }

    public List<SentenceNode> getSentences() {
        return sentences;
    }

    public void addSentence(SentenceNode sentence) {
        sentences.add(sentence);
    }
}
