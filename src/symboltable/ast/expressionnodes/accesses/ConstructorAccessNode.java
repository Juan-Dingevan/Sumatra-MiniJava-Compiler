package symboltable.ast.expressionnodes.accesses;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.UndeclaredClassException;
import symboltable.ast.chaining.ChainingNode;
import symboltable.ast.expressionnodes.AccessNode;
import symboltable.ast.expressionnodes.ExpressionNode;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.members.Constructor;
import symboltable.table.SymbolTable;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import token.Token;
import utility.ActualArgumentsHandler;

import java.util.List;

public class ConstructorAccessNode extends AccessNode {
    protected Token classToken;

    protected List<ExpressionNode> actualArguments;

    public Token getClassToken() {
        return classToken;
    }

    public void setClassToken(Token classToken) {
        this.classToken = classToken;
    }

    @Override
    protected Type accessCheck() throws CompilerException {
        //TODO: make the necessary checks
        String referenceName = classToken.getLexeme();
        ConcreteClass classConstructed = SymbolTable.getInstance().getClass(referenceName);
        boolean classExists = classConstructed != null;

        if(!classExists)
            throw new UndeclaredClassException(classToken);

        Constructor constructor = classConstructed.getConstructor();

        ActualArgumentsHandler.checkActualArguments(constructor, actualArguments);

        return new ReferenceType(classToken.getLexeme());
    }

    public void setActualArguments(List<ExpressionNode> actualArguments) {
        this.actualArguments = actualArguments;
    }

    @Override
    public boolean isValidAsSentence() {
        if(chainingNode != ChainingNode.NO_CHAINING)
            return chainingNode.isCall();
        else
            return true;
    }

}
