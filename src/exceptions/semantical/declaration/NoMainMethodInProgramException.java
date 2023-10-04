package exceptions.semantical.declaration;

import token.Token;

public class NoMainMethodInProgramException extends DeclarationException {
    public NoMainMethodInProgramException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "There is no main method in program.";
    }
}
