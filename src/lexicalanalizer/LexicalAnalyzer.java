package lexicalanalizer;

import exceptions.general.CompilerException;
import token.Token;

public interface LexicalAnalyzer {
    Token getNextToken() throws CompilerException;
}
