package exceptions.semantical;

import token.Token;

public class NoMainMethodInProgramException extends SemanticException {
    public NoMainMethodInProgramException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "There is no main method in program.";
    }
}
