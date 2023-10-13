package symboltable.types;

public class Int extends SInteger {
    public String toString() {
        return "int";
    }
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Int;
    }
}
