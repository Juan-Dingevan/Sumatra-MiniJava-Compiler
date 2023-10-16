package symboltable.types;

public class NullType extends ReferenceType{
    public NullType() {
        super("NULL");
    }

    @Override
    public String toString() {
        return "NULL";
    }
}
