package reservedwordstable;

import token.TokenType;
import java.util.Map;
import static token.TokenType.*;

public class ReservedWordsTable {
    private static final Map<String, TokenType> reservedWordTable = Map.ofEntries(
            Map.entry("class", reserved_word_class),
            Map.entry("interface", reserved_word_interface),
            Map.entry("extends", reserved_word_extends),
            Map.entry("implements", reserved_word_implements),
            Map.entry("public", reserved_word_public),
            Map.entry("private", reserved_word_private),
            Map.entry("static", reserved_word_static),
            Map.entry("void", reserved_word_void),
            Map.entry("boolean", reserved_word_boolean),
            Map.entry("char", reserved_word_char),
            Map.entry("int", reserved_word_int),
            Map.entry("float", reserved_word_float),
            Map.entry("if", reserved_word_if),
            Map.entry("else", reserved_word_else),
            Map.entry("while", reserved_word_while),
            Map.entry("return", reserved_word_return),
            Map.entry("var", reserved_word_var),
            Map.entry("this", reserved_word_this),
            Map.entry("new", reserved_word_new),
            Map.entry("null", reserved_word_null),
            Map.entry("true", reserved_word_true),
            Map.entry("false", reserved_word_false)
    );
    public static TokenType lookUp(String identifier) {
        TokenType lookedUpValue = reservedWordTable.get(identifier);
        return lookedUpValue == null ? id_method_variable : lookedUpValue;
    }
}
