package symboltable.ast.expressionnodes.literals;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import symboltable.ast.expressionnodes.LiteralNode;
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
        String lexeme = token.getLexeme();
        String tag = "str_" + id;

        CodeGenerator.getInstance().append(".DATA");
        CodeGenerator.getInstance().append(tag + ": DW " + lexeme + ",0");

        CodeGenerator.getInstance().append(".CODE");

        String cPush = " # We leave a reference to the String we created at the top of the stack";
        CodeGenerator.getInstance().append("PUSH " + tag + cPush);
    }
}
