package symboltable.types;

public class Void extends Type {
    public String toString() {
        return "void";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Void;
    }
}
