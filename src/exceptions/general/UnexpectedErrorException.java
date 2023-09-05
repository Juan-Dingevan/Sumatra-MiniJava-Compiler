package exceptions.general;

public class UnexpectedErrorException extends CompilerException {
    private final String whatWasHappening;
    public UnexpectedErrorException(String whatWasHappening) {
        this.whatWasHappening = whatWasHappening;
    }
    @Override
    public String getMessage() {
        return "An unexpected error (not related to the compilation itself) occurred while "+whatWasHappening+".";
    }
}
