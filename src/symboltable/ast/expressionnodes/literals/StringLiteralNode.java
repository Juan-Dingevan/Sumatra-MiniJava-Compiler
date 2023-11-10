package symboltable.ast.expressionnodes.literals;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import symboltable.ast.expressionnodes.LiteralNode;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.members.Constructor;
import symboltable.table.SymbolTable;
import symboltable.types.ReferenceType;
import symboltable.types.Type;

public class StringLiteralNode extends LiteralNode {
    private static int classID = 0;
    private int id;

    public StringLiteralNode() {
        id = classID;
        classID++;
    }

    @Override
    public Type check() throws CompilerException {
        return new ReferenceType("String");
    }

    @Override
    public void generate() throws CompilerException {
        ConcreteClass classConstructed = SymbolTable.getInstance().getClass("String");

        String lexeme = token.getLexeme();
        String tag;

        if(lexeme.equals("")) {
            tag = CodeGenerator.EMPTY_STRING_TAG;
        } else {
            tag = "str_" + id;
            CodeGenerator.getInstance().append(".DATA");
            CodeGenerator.getInstance().append(tag + ": DW " + lexeme + ",0");
        }

        //We're doing an implicit constructor access here!
        CodeGenerator.getInstance().append(".CODE");

        String cReserve = " # In String Literal: We reserve a memory cell to store the pointer to the constructed object";
        CodeGenerator.getInstance().append("RMEM 1" + cReserve);

        String c2 = " # In String Literal: We load malloc's parameter (hardcoded)";
        CodeGenerator.getInstance().append("PUSH 2"+ c2);

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

        String c8 = " # We duplicate malloc's return and we use it as the 'this' reference for the constructor";
        CodeGenerator.getInstance().append("DUP" + c8);

        //This is sort of a hard-coded constructor call. We don't *actually* call it,
        //but we manipulate the CIR as though we were the constructor.

        String c9 = " # We put the String tag at the top of the stack";
        CodeGenerator.getInstance().append("PUSH " + tag + c9);

        String c10 = " # We link the tag to the 'fake attribute'";
        CodeGenerator.getInstance().append("STOREREF 1" + c10);

    }
}
