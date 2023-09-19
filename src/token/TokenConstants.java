package token;

public abstract class TokenConstants {
    public static final Token OBJECT_TOKEN = new Token(TokenType.id_class, "Object", -1);
    public static final Token STRING_TOKEN = new Token(TokenType.id_class, "String", -1);
    public static final Token SYSTEM_TOKEN = new Token(TokenType.id_class, "System", -1);

    public static final Token DEBUG_PRINT_TOKEN = new Token(TokenType.id_method_variable, "debugPrint", -1);

    public static final Token READ_TOKEN = new Token(TokenType.id_method_variable, "read", -1);

    public static final Token PRINTB_TOKEN = new Token(TokenType.id_method_variable, "printB", -1);
    public static final Token PRINTC_TOKEN = new Token(TokenType.id_method_variable, "printC", -1);
    public static final Token PRINTI_TOKEN = new Token(TokenType.id_method_variable, "printI", -1);
    public static final Token PRINTS_TOKEN = new Token(TokenType.id_method_variable, "printS", -1);

    public static final Token PRINTLN_TOKEN = new Token(TokenType.id_method_variable, "println", -1);

    public static final Token PRINTBLN_TOKEN = new Token(TokenType.id_method_variable, "printBln", -1);
    public static final Token PRINTCLN_TOKEN = new Token(TokenType.id_method_variable, "printCln", -1);
    public static final Token PRINTILN_TOKEN = new Token(TokenType.id_method_variable, "printIln", -1);
    public static final Token PRINTSLN_TOKEN = new Token(TokenType.id_method_variable, "printSln", -1);

    public static final Token PARAMETER_B_TOKEN = new Token(TokenType.id_method_variable, "b", -1);
    public static final Token PARAMETER_C_TOKEN = new Token(TokenType.id_method_variable, "b", -1);
    public static final Token PARAMETER_I_TOKEN = new Token(TokenType.id_method_variable, "b", -1);
    public static final Token PARAMETER_S_TOKEN = new Token(TokenType.id_method_variable, "b", -1);
}
