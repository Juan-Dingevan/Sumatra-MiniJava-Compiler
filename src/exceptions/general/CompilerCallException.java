package exceptions.general;

public class CompilerCallException extends CompilerException{
    private final int numberOfArguments;
    public CompilerCallException(int numberOfArguments) {
        super();
        this.numberOfArguments = numberOfArguments;
    }
    public String getMessage() {
        return "Error on compiler call. Expected 1 argument, got: " + numberOfArguments + ".";
    }
}
