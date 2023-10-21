package symboltable.ast.expressionnodes.accesses;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.PrivateMemberAccessException;
import exceptions.semantical.sentence.UndeclaredClassException;
import symboltable.ast.chaining.ChainingNode;
import symboltable.ast.expressionnodes.AccessNode;
import symboltable.ast.expressionnodes.ExpressionNode;
import symboltable.privacy.Privacy;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.members.Constructor;
import symboltable.table.SymbolTable;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import token.Token;
import utility.ActualArgumentsHandler;

import java.util.ArrayList;
import java.util.List;

public class ConstructorAccessNode extends AccessNode {
    public static final List<String> NO_GENERIC_TYPES = new ArrayList<>();
    protected Token classToken;

    protected List<ExpressionNode> actualArguments;
    protected List<String> genericInstantiation;

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

        Privacy privacy = constructor.getPrivacy();

        boolean accessedFromDeclaringClass = contextClass == constructor.getMemberOf();
        boolean isPrivate = privacy != Privacy.publicS;

        if(isPrivate && !accessedFromDeclaringClass) {
            throw new PrivateMemberAccessException(token);
        }

        /*
            Chequeos genericos a hacer
                - que si la clase de la que se usa el constr. tiene parametros de tipo genericos
                  que la llamada tenga, o bien la misma cantidad, o la notación diamante
                - que si tiene la notación diamante, se puedan inferir los tipos ¿como?
                - al Type retornado cargarle los parametros de tipo genericos
         */

        ActualArgumentsHandler.checkActualArguments(constructor, actualArguments, classToken);

        return new ReferenceType(classToken.getLexeme());
    }

    public void setActualArguments(List<ExpressionNode> actualArguments) {
        this.actualArguments = actualArguments;
    }

    public void setGenericInstantiation(List<String> genericInstantiation) {
        this.genericInstantiation = genericInstantiation;
    }

    @Override
    public boolean isValidAsSentence() {
        if(chainingNode != ChainingNode.NO_CHAINING)
            return chainingNode.isCall();
        else
            return true;
    }

}
