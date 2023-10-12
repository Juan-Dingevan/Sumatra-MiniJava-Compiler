package symboltable.ast.chaining;

public class MethodChainingNode extends ChainingNode{
    public String getDeclarationForm() {
        String s = "." + token.getLexeme() + "(...)";
        String c = chainingNode == ChainingNode.NO_CHAINING ? "" : chainingNode.getDeclarationForm();
        return s + c;
    }

    @Override
    public boolean isCall() {
        if(hasChaining())
            return chainingNode.isCall();
        else
            return true;
    }
}
