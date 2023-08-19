package lexicalanalizer;

import exceptions.CompilerException;
import token.Token;

public interface LexicalAnalyzer {
    Token getNextToken() throws CompilerException;
}
