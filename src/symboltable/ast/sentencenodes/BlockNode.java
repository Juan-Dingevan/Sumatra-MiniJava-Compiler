package symboltable.ast.sentencenodes;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.LocalVariableAlreadyExistsException;
import symboltable.symbols.members.Variable;

import java.util.*;

public class BlockNode extends SentenceNode{
    public static final BlockNode NULL_PARENT = new BlockNode();
    public static int classID = 0;
    private final int id;
    protected List<SentenceNode> sentences;
    protected Map<String, Variable> variables;

    public BlockNode() {
        sentences = new ArrayList<>();
        variables = new HashMap<>();
        id = classID;
        classID++;
    }

    @Override
    protected int getID() {
        return id;
    }

    public List<SentenceNode> getSentences() {
        return sentences;
    }
    public Collection<Variable> getVariables() {
        return variables.values();
    }

    public void addSentence(SentenceNode sentence) {
        sentences.add(sentence);
    }

    public boolean existsInScope(Variable v) {
        if(existsLocally(v))
            return true;

        if(parentBlock == NULL_PARENT)
            return false;

        return parentBlock.existsLocally(v);
    }

    public boolean existsLocally(Variable v) {
        return variables.get(v.getName()) != null;
    }
    public void addLocalVariable(Variable v) throws CompilerException {
        if(!existsInScope(v)) {
            variables.put(v.getName(), v);
        } else {
            throw new LocalVariableAlreadyExistsException(v.getToken());
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(super.toString());

        if(variables.size() > 0) {
            sb.append("variables: \n");
            sb.append("\t");

            for(Variable v : getVariables()) {
                sb.append(v.getName());
                sb.append(" ");
            }

            sb.append("\n");
            sb.append("\n");
        }

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
}
