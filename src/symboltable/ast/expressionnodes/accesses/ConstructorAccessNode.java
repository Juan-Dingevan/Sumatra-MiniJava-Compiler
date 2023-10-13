package symboltable.ast.expressionnodes.accesses;

import exceptions.general.CompilerException;
import symboltable.ast.chaining.ChainingNode;
import symboltable.ast.expressionnodes.AccessNode;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import token.Token;

public class ConstructorAccessNode extends AccessNode {
    protected Token classToken;

    public Token getClassToken() {
        return classToken;
    }

    public void setClassToken(Token classToken) {
        this.classToken = classToken;
    }

    @Override
    protected Type accessCheck() throws CompilerException {
        //TODO: make the necessary checks
        return new ReferenceType(classToken.getLexeme());
    }

    @Override
    public boolean isValidAsSentence() {
        if(chainingNode != ChainingNode.NO_CHAINING)
            return chainingNode.isCall();
        else
            return true;
    }

    public String getDeclarationForm() {
        return token.getLexeme() + " " + classToken.getLexeme() + "(...)" + getChainingDeclarationForm();
    }
}
