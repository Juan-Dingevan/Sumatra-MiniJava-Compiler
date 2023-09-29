package symboltable.types;

import symboltable.symbols.classes.Class;
import symboltable.symbols.members.TypedEntity;

public abstract class Type {
    public static boolean isReferenceType(Type t) {
        return t instanceof ReferenceType;
    }

    public static boolean isVoid(Type t) {
        return t instanceof Void;
    }

    public static boolean typedEntitiesAreEquivalentInContext(TypedEntity te1, TypedEntity te2, Class contextClass) {
        Type t1 = te1.getType();
        Type t2 = te2.getType();

        return typesAreEquivalentInContext(t1, t2, contextClass);
    }

    public static boolean typesAreEquivalentInContext(Type t1, Type t2, Class contextClass) {
        if(Type.isReferenceType(t1) != Type.isReferenceType(t2))
            return false; //uno es ref-type y el otro no, trivialmente son distintos

        if(!Type.isReferenceType(t1))
            return t1.equals(t2); //caso trivial 1: ninguno es ref type

        //a partir de aca podemos asumir que ambos son ref-type
        //hacemos el cast y trabajamos con los rt directamente
        ReferenceType rt1 = (ReferenceType) t1;
        ReferenceType rt2 = (ReferenceType) t2;

        return contextClass.referenceTypesAreEquivalentInClass(rt1, rt2);
    }
}
