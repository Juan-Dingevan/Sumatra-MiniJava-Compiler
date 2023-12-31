package symboltable.types;

//Same as with SFloat. We add Sumatra's initial S to
//differentiate from Java's native Boolean class.
public class SBoolean extends PrimitiveType{
    public String toString() {
        return "boolean";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SBoolean;
    }
}
