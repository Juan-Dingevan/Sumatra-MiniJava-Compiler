package symboltable.ast.sentencenodes;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.LocalVariableAlreadyExistsException;
import symboltable.symbols.members.Member;
import symboltable.symbols.members.Variable;

import java.util.*;

public class BlockNode extends SentenceNode{
    public static final BlockNode NULL_PARENT = new BlockNode();
    public static int classID = 0;
    private final int id;
    protected List<SentenceNode> sentences;
    protected Map<String, Variable> variables;
    protected List<Variable> variableList;
    private int offset;

    public BlockNode() {
        sentences = new ArrayList<>();
        variables = new HashMap<>();
        variableList = new ArrayList<>();
        offset = Member.LOCAL_VAR_MIN_OFFSET;
        id = classID;
        classID++;
    }

    @Override
    protected int getID() {
        return id;
    }

    @Override
    protected void checkSelf() throws CompilerException {
        for(SentenceNode s : sentences)
            s.check();
    }

    public void giveLocalVariablesOffset() {
        if(parentBlock != NULL_PARENT) {
            offset = parentBlock.getOffset();
            System.out.println("Block " + id + " inherits " + offset + " from " + parentBlock.id);
        }

        for(Variable variable : variableList) {
            variable.setOffset(offset);
            offset--;
        }

        for(SentenceNode s : sentences) {
            System.out.println("In " + id + " calling giveLocal...() of " + s.getClass().getSimpleName() + s.getID());
            s.giveLocalVariablesOffset();
        }

        System.out.println("Block " + id + " has offset " + offset);
    }

    public int getOffset() {
        return offset;
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

        return parentBlock.existsInScope(v);
    }

    public boolean existsLocally(Variable v) {
        return variables.get(v.getName()) != null;
    }

    public Variable getLocalVariable(String name) {
        Variable v = variables.get(name);

        if(v != null)
            return v;

        if(parentBlock == NULL_PARENT)
            return null;

        return parentBlock.getLocalVariable(name);
    }

    public void addLocalVariable(Variable v) throws CompilerException {
        if(!existsInScope(v)) {
            variables.put(v.getName(), v);
            variableList.add(v);
        } else {
            throw new LocalVariableAlreadyExistsException(v.getToken());
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(super.toString());

        if(variables.size() > 0) {
            sb.append(tabs());
            sb.append("variables: \n");
            sb.append(tabs());
            sb.append("\t");

            for(Variable v : getVariables()) {
                sb.append(v.getName());
                sb.append(" [");
                sb.append(v.getOffset());
                sb.append("] ");
            }

            sb.append("\n");
            sb.append("\n");
        }

        if(sentences.size() > 0) {
            sb.append(tabs());
            sb.append("children:\n");

            for(SentenceNode s : sentences) {
                if(s != null) {
                    s.stringDepth = stringDepth + 1;
                    sb.append(s);
                    sb.append("\n");
                } else {
                    sb.append("null\n");
                }
            }
        } else {
            sb.append("[empty]\n");
        }

        return sb.toString();
    }
}
