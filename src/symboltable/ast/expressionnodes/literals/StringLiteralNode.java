package symboltable.ast.expressionnodes.literals;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import symboltable.ast.expressionnodes.LiteralNode;
import symboltable.types.ReferenceType;
import symboltable.types.Type;

public class StringLiteralNode extends LiteralNode {
    @Override
    public Type check() throws CompilerException {
        return new ReferenceType("String");
    }

    public void generate() throws CompilerException {
        String lexeme = token.getLexeme();
        String lexemeWithoutQuotes = lexeme.substring(1, lexeme.length()-1);

        CodeGenerator.getInstance().append("DW " + lexemeWithoutQuotes + ",0");
    }
}
