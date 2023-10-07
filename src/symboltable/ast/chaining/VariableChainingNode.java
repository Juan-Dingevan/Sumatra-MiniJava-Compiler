package symboltable.ast.chaining;

public class VariableChainingNode extends ChainingNode{
    public String getDeclarationForm() {
        String s = "." + token.getLexeme();
        String c = chainingNode == ChainingNode.NO_CHAINING ? "" : chainingNode.getDeclarationForm();
        return s + c;
    }
}