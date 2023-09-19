package symboltable.types;

public abstract class Type {
    public static boolean isReferenceType(Type t) {
        return t instanceof ReferenceType;
    }

    public static boolean isVoid(Type t) {
        return t instanceof Void;
    }
}
