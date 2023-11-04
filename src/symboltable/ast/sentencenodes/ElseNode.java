package symboltable.ast.sentencenodes;

import exceptions.general.CompilerException;

public class ElseNode extends SentenceNode {
    public static final ElseNode NULL_ELSE = new ElseNode();
    protected BlockNode implicitBlock;

    public static int classID = 0;
    private final int id = classID;
    @Override
    protected int getID() {
        return id;
    }

    public ElseNode() {
        implicitBlock = new BlockNode();
    }

    @Override
    public void setParentBlock(BlockNode parentBlock) {
        super.setParentBlock(parentBlock);
        implicitBlock.setParentBlock(parentBlock);
    }
    public BlockNode getImplicitBlock() {
        return implicitBlock;
    }

    public void setSentence(SentenceNode sentence) {
        if(sentence != SEMICOLON_SENTENCE)
            implicitBlock.addSentence(sentence);
    }

    @Override
    protected void checkSelf() throws CompilerException {
        implicitBlock.check();
    }

    @Override
    public void generate() throws CompilerException {
        implicitBlock.generate();
    }

    @Override
    public void giveLocalVariablesOffset() {
        implicitBlock.giveLocalVariablesOffset();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());

        sb.append("\n");
        sb.append(tabs());
        sb.append("implicitBlock:\n");

        if(implicitBlock != null) {
            implicitBlock.stringDepth = stringDepth + 1;
            sb.append(implicitBlock);
            sb.append("\n");
        } else {
            sb.append("null\n");
        }

        return sb.toString();
    }
}
