package symboltable.types;

public class Char extends SInteger {
    public String toString() {
        return "char";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Char;
    }
}
