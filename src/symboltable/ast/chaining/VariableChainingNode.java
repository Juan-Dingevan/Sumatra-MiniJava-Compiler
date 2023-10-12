package symboltable.ast.chaining;

public class VariableChainingNode extends ChainingNode{

    @Override
    public boolean isCall() {
        if(hasChaining())
            return chainingNode.isCall();
        else
            return true;
    }

    public String getDeclarationForm() {
        String s = "." + token.getLexeme();
        String c = chainingNode == ChainingNode.NO_CHAINING ? "" : chainingNode.getDeclarationForm();
        return s + c;
    }
}
