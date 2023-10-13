package symboltable.types;

//In order not to be bothered by Java's native Float class
//We prefix it with Sumatra's initial: S
public class SFloat extends SNumber {
    public String toString() {
        return "float";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SFloat;
    }
}
