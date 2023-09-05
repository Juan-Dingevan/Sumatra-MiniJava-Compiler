package syntaxanalyzer;

import exceptions.general.CompilerException;
import exceptions.syntax.SyntaxException;

public interface SyntaxAnalyzer {
    void analyze() throws SyntaxException, CompilerException;
}
