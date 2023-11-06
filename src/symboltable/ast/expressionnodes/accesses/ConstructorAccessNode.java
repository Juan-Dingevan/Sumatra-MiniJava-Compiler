package symboltable.ast.expressionnodes.accesses;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import exceptions.semantical.declaration.GenericsException;
import exceptions.semantical.sentence.PrivateMemberAccessException;
import exceptions.semantical.sentence.UndeclaredClassException;
import symboltable.ast.chaining.ChainingNode;
import symboltable.ast.expressionnodes.AccessNode;
import symboltable.ast.expressionnodes.ExpressionNode;
import symboltable.privacy.Privacy;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.members.Attribute;
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
    protected ConcreteClass classConstructed;
    protected Constructor constructor;
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
        classConstructed = SymbolTable.getInstance().getClass(referenceName);

        boolean classExists = classConstructed != null;
        if(!classExists)
            throw new UndeclaredClassException(classToken);

        constructor = classConstructed.getConstructor();

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

    @Override
    protected void accessGenerate() throws CompilerException {
        String cReserve = " # We reserve a memory cell to store the pointer to the constructed object";
        CodeGenerator.getInstance().append("RMEM 1" + cReserve);

        int instanceVariableCounter = 0;
        for(Attribute attribute : classConstructed.getAttributes()) {
            if(!attribute.isStatic())
                instanceVariableCounter++;
        }
        int vTableSpace = 1;
        int reservedCells = instanceVariableCounter + vTableSpace;

        String c2 = " # We load malloc's parameter: the number of cells to reserve for the constructed object";
        CodeGenerator.getInstance().append("PUSH " + reservedCells + c2);

        String mallocTag = CodeGenerator.getMallocTag();

        String c3 = " # We put malloc's tag on top of the stack";
        CodeGenerator.getInstance().append("PUSH " + mallocTag + c3);

        String c4 = " # We make the call";
        CodeGenerator.getInstance().append("CALL" + c4);

        //Now, we must link the VTable of the class of the object that's being constructed to its CIR

        String c5 = " # We duplicate malloc's return so we don't lose it on our next instr.";
        CodeGenerator.getInstance().append("DUP" + c5);

        String vTableTag = CodeGenerator.getVTableTag(classConstructed);

        String c6 = " # We push the VTable's tag so we can link it to the CIR.";
        CodeGenerator.getInstance().append("PUSH " + vTableTag + c6);

        String c7 = " # We store the reference (tag) to the VTable in the CIR of the constructed object (the offset is hard-coded)";
        CodeGenerator.getInstance().append("STOREREF 0" + c7);

        //Now, we must make the call to the constructor itself.

        String c8 = " # We duplicate malloc's return and we use it as the 'this' reference for the constructor";
        CodeGenerator.getInstance().append("DUP" + c8);

        String c9 = " # We swap to keep the 'this' reference at the top of the stack";
        for(ExpressionNode argument : actualArguments) {
            argument.generate();
            CodeGenerator.getInstance().append("SWAP " + c9);
        }

        String constructorTag = CodeGenerator.getConstructorTag(constructor);

        String c10 = " # We put the constructor's tag on top of the stack";
        CodeGenerator.getInstance().append("PUSH " + constructorTag + c10);

        String c11 = " # We make the call";
        CodeGenerator.getInstance().append("CALL" + c11);
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
