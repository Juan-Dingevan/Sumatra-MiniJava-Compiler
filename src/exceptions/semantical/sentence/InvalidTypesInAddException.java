package exceptions.semantical.sentence;

import token.Token;

public class InvalidTypesInAddException extends InvalidTypesInBinaryOperatorException{
    public InvalidTypesInAddException(Token t) {
        super(t, null, null);
    }

    @Override
    public String getSpecificMessage() {
        return "Error in line " + lineNumber + " the + operator can be used between numeric types (int, char, float) " +
               "or to concatenate a char or an int to a String.";
    }
}
