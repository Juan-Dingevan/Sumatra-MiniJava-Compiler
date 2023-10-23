package symboltable.ast.expressionnodes.accesses;

import exceptions.general.CompilerException;
import exceptions.semantical.declaration.GenericsException;
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
    public static final List<String> DIAMOND_NOTATION = new ArrayList<>();
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

        List<String> genericTypes = classConstructed.getGenericTypes();

        if(genericTypes.size() != 0 && genericInstantiation == NO_GENERIC_TYPES) {
            //caso 1: la clase tiene generic types y no se instancian
            String error = "Error in line " + token.getLineNumber() + " the constructor doesn't have the correct number of generic type parameters for generic class instantiation.";
            throw new GenericsException(token, error);
        }

        if(!usesDiamondNotation() && genericTypes.size() != genericInstantiation.size()) {
            //caso 2: la clase tiene generic types y se instancian, pero con aridad incorrecta
            String error = "Error in line " + token.getLineNumber() + " the constructor doesn't have the correct number of generic type parameters for generic class instantiation.";
            throw new GenericsException(token, error);
        }

        for(String typeParameter : genericInstantiation) {
            boolean isClassOrInterface = SymbolTable.getInstance().exists(typeParameter);
            boolean isGenericType = contextClass.isGenericType(typeParameter);

            if(!(isClassOrInterface || isGenericType)) {
                String error = "Error in line " + token.getLineNumber() + " the type parameter " + typeParameter + " used to instantiate generics" +
                               "isn't a declared class or interface, or a generic parameter of the current class.";
                throw new GenericsException(token, error);
            }
        }

        /*
        * Con lo del diamante hacer lo siguiente: no chequear los argumentos de manera directa,
        * sino mandarlos a chequear desde el que infirio los tipos.
        * */

        ReferenceType rt = new ReferenceType(classToken.getLexeme());
        rt.setGenericTypes(genericInstantiation);

        ActualArgumentsHandler.checkActualArguments(constructor, actualArguments, classToken, rt);

        return rt;
    }

    private boolean usesDiamondNotation() {
        return genericInstantiation == DIAMOND_NOTATION;
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
