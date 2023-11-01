package symboltable.ast.expressionnodes.literals;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import symboltable.ast.expressionnodes.LiteralNode;
import symboltable.types.Char;
import symboltable.types.Type;

public class CharLiteralNode extends LiteralNode {
    @Override
    public Type check() throws CompilerException {
        return new Char();
    }

    public void generate() throws CompilerException {
        String lexeme = token.getLexeme();
        String lexemeWithoutQuotes = lexeme.substring(1, lexeme.length()-1);
        char charLiteral = lexemeWithoutQuotes.charAt(0);
        int characterAsNumber = charLiteral;

        CodeGenerator.getInstance().append("PUSH " + characterAsNumber);
    }
}
