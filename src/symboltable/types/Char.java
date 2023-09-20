package symboltable.types;

public class Char extends Type{
    public String toString() {
        return "char";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Char;
    }
}
