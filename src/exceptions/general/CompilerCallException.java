package exceptions.general;

public class CompilerCallException extends CompilerException{
    private final int numberOfArguments;
    public CompilerCallException(int numberOfArguments) {
        super();
        this.numberOfArguments = numberOfArguments;
    }
    public String getMessage() {
        return "Error on compiler call. Expected at least 1 argument and at most 2. Got: " + numberOfArguments + ".";
    }
}
