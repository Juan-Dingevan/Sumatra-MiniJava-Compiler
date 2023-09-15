package utility;

public class StringUtilities {
    public static String repeatCharacter(char character, int times) {
        String repeatedChar = "";

        for(int i = 0; i < times; i++)
            repeatedChar += character;

        return repeatedChar;
    }
    public static Pair<Integer, String> removeAllConsecutiveWhitespacesAtTheStartOfString(String stringToClean) {
        int i = 0;
        while(i < stringToClean.length() && CharacterIdentifier.isWhitespace(stringToClean.charAt(i)))
            i++;

        Pair<Integer, String> p = new Pair<>(i, stringToClean.substring(i));

        return p;
    }
    public static void setTextToWhite() {
        System.out.print("\u001B[0m");
    }
    public static void setTextToRed() {
        System.out.print("\u001B[31m");
    }
    public static void setTextToGreen() {
        System.out.print("\u001B[32m");
    }
    public static String getDashesForDepth(int depth) {
        String s = "";

        for(int i = 0; i < depth; i++)
            s += "----";

        return s;
    }

}

