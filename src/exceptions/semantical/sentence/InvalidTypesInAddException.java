package exceptions.semantical.sentence;

import token.Token;

public class InvalidTypesInAddException extends InvalidTypesInBinaryOperatorException{
    public InvalidTypesInAddException(Token t) {
        super(t, null, null);
    }

    @Override
    public String getSpecificMessage() {
        return "Error in line " + lineNumber + " the + operator expects at least one numeric operand (int, char, float)";
    }
}
