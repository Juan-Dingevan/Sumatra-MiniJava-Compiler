package utility;

public class CharacterIdentifier {
    private static final String UPPER_CASE_LETTERS = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
    private static final String LOWER_CASE_LETTERS = "abcdefghijklmnñopqrstuvwxyz";
    private static final String OPERAND_CHARACTERS = "+-=/*&|!<>%";
    private static final String PUNCTUATION_CHARACTERS = "(){};,.";
    private static final char SINGLE_QUOTATION = '\'';
    private static final char DOUBLE_QUOTATION = '"';
    private static final char BACKWARDS_BAR = '\\';
    private static final char SPACE = ' ';
    private static final char TAB = '\t';
    private static final char EOL = '\n';
    private static final String WHITE_SPACES = "" + SPACE + TAB + EOL;
    private static final String DIGITS = "0123456789";
    private static final String OTHER_VALID_CHARS = "";
    public static final char END_OF_FILE = (char) 26;

    private static final String ALL_VALID_CHARS = UPPER_CASE_LETTERS +
            LOWER_CASE_LETTERS +
            OPERAND_CHARACTERS +
            PUNCTUATION_CHARACTERS +
            SINGLE_QUOTATION +
            DOUBLE_QUOTATION +
            BACKWARDS_BAR +
            SPACE +
            TAB +
            EOL +
            DIGITS +
            OTHER_VALID_CHARS;

    //All characters that, when processing a literal_char are not considered special cases.
    private static final String LITERAL_CHAR_VALID_CHARS = ALL_VALID_CHARS
            .replace(""+BACKWARDS_BAR, "")
            .replace(""+SINGLE_QUOTATION, "");

    //All characters that, when processing a literal_string are not considered special cases.
    private static final String LITERAL_STRING_VALID_CHARS = ALL_VALID_CHARS
            .replace(""+BACKWARDS_BAR, "");

    public static boolean isValidCharacter(char c) {
        return ALL_VALID_CHARS.contains(""+c);
    }
    public static boolean isUpperCaseLetter(char c) {
        return UPPER_CASE_LETTERS.contains(""+c);
    }
    public static boolean isLowerCaseLetter(char c) {
        return LOWER_CASE_LETTERS.contains(""+c);
    }
    public static boolean isDigit(char c) {
        return DIGITS.contains(""+c);
    }
    public static boolean isWhitespace(char c) {
        return WHITE_SPACES.contains(""+c);
    }
    public static boolean isCharLiteralNormalCase(char c) {
        return LITERAL_CHAR_VALID_CHARS.contains("" + c);
    }
    public static boolean isStringLiteralNormalCase(char c) {
        return LITERAL_STRING_VALID_CHARS.contains("" + c);
    }
    public static boolean isSingleQuotation(char c) {
        return SINGLE_QUOTATION == c;
    }
    public static boolean isDoubleQuotation(char c) {
        return DOUBLE_QUOTATION == c;
    }
    public static boolean isBackwardsBar(char c) {
        return BACKWARDS_BAR == c;
    }
    public static boolean isEOL(char c) {
        return EOL == c;
    }
    public static boolean isEOF(char c) {
        return c == END_OF_FILE;
    }
}
