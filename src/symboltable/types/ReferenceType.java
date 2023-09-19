package symboltable.types;

public class ReferenceType extends Type{
    String referenceName;

    public ReferenceType(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public String toString() {
        return "referenceType(" + referenceName + ")";
    }
}
