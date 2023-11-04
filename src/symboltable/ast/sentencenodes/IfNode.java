package symboltable.ast.sentencenodes;

import codegenerator.CodeGenerator;
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

    @Override
    public void generate() throws CompilerException {
        if(elseNode == ElseNode.NULL_ELSE)
            generateIfThen();
        else
            generateIfThenElse();
    }

    private void generateIfThenElse() throws CompilerException {
        String tagOut  = "out_if_" + id;
        String tagElse = "else_if_" + id;

        expression.generate();
        CodeGenerator.getInstance().append("BF " + tagElse);
        implicitBlock.generate();
        CodeGenerator.getInstance().append("JUMP " + tagOut);
        CodeGenerator.getInstance().append(tagElse + ": NOP");
        elseNode.generate();
        CodeGenerator.getInstance().append(tagOut + ": NOP");
    }

    private void generateIfThen() throws CompilerException {
        String tag = "out_if_" + id;

        expression.generate();
        CodeGenerator.getInstance().append("BF " + tag);
        implicitBlock.generate();
        CodeGenerator.getInstance().append(tag + ": NOP");
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
