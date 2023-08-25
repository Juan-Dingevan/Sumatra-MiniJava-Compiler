package utility;

public class FloatingPointNumberValidator {
    private static double MINIMUM_VALUE = (double) Float.MIN_VALUE;
    private static double MAXIMUM_VALUE = (double) Float.MAX_VALUE;
    public static boolean isIEEE754(String input) {
        try {
            double value = Double.parseDouble(input);

            if (Double.isFinite(value)) {
                if (value == 0.0 || (Math.abs(value) >= MINIMUM_VALUE && Math.abs(value) <= MAXIMUM_VALUE)) {
                    return true;
                }
            }

            /*
            * Probar pensando en como van a ser los programas; cosas pegadas: int+String+id
            * Puntos y comas mezclando cosas
            * */

            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
