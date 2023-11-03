package symboltable.ast.sentencenodes;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.TypesDontConformException;
import symboltable.types.SBoolean;
import symboltable.types.Type;

public class IfNode extends ControlStructureNode {
    public static int classID = 0;
    private final int id = classID;
    protected ElseNode elseNode;
    @Override
    protected int getID() {
        return id;
    }

    public ElseNode getElseNode() {
        return elseNode;
    }

    public void setElseNode(ElseNode elseNode) {
        this.elseNode = elseNode;
    }

    @Override
    protected void checkSelf() throws CompilerException {
        super.checkSelf();
        if(elseNode != ElseNode.NULL_ELSE)
            elseNode.check();
    }

    @Override
    public void giveLocalVariablesOffset() {
        super.giveLocalVariablesOffset();
        if(elseNode != ElseNode.NULL_ELSE)
            elseNode.giveLocalVariablesOffset();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());

        if(elseNode != ElseNode.NULL_ELSE) {
            sb.append("\n");
            sb.append(tabs());
            sb.append("else:\n");
            elseNode.stringDepth = stringDepth + 1;
            sb.append(elseNode);
        }

        return sb.toString();
    }
}
