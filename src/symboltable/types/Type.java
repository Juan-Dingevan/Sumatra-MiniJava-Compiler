package symboltable.types;

import symboltable.symbols.classes.Class;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.members.TypedEntity;
import symboltable.table.SymbolTable;

public abstract class Type {
    public static boolean isReferenceType(Type t) {
        return t instanceof ReferenceType;
    }
    public static boolean isVoid(Type t) {
        return t instanceof Void;
    }
    public static boolean isNumber(Type t) {
        return t instanceof SNumber;
    }
    public static boolean isNull(Type t) {
        return t instanceof NullType;
    }

    public static boolean typesConformInContext(Type t1, Type t2, ConcreteClass context) {
        //returns true if t1 conforms to t2
        if(!isReferenceType(t1) && !isReferenceType(t2)) {
            //trivial case: if neither t1 nor t2 are reference types,
            //they will conform to each other if and only if they are of the same type
            //or if they can both be coerced into numbers (char conforms to int, int to float)

            boolean t1char = t1.equals(new Char());
            boolean t1int = t1.equals(new Int());

            if(t1.equals(t2))
                return true;
            else if(t1char)
                return isNumber(t2);
            else if(t1int)
                return t2.equals(new SFloat());
            else
                return false;
        }

        if(!isReferenceType(t1) && isReferenceType(t2)) {
            //the ONLY cases where a non-reference type conforms to a reference type
            //is when we are coercing char or int into String
            boolean t1Int = t1.equals(new Int());
            boolean t1char = t1.equals(new Char());

            boolean coercibleToString = t1Int || t1char;
            boolean t2String = ((ReferenceType) t2).getReferenceName().equals("String");

            return coercibleToString && t2String;
        }

        if(isReferenceType(t1) && !isReferenceType(t2)) {
            //there are NO cases where a rf conforms to a non-rf
            return false;
        }

        if(isReferenceType(t1) && isReferenceType(t2)) {
            ReferenceType rt1 = (ReferenceType) t1;
            ReferenceType rt2 = (ReferenceType) t2;

            if(isNull(rt1)) {
                //trivially, Null conforms to any reference type.
                return true;
            }

            boolean genericArityExists = rt1.getGenericTypes().size() > 0 || rt2.getGenericTypes().size() > 0;
            boolean genericTypes = context.isGenericType(rt1.getReferenceName()) || context.isGenericType(rt2.getReferenceName());

            if(genericTypes) {
                return typesAreEquivalentInContext(t1, t2, context);
            }

            if(genericArityExists) {
                return context.referenceTypesConformInClass(rt1, rt2);
            }

            Class c1 = SymbolTable.getInstance().getClassOrInterface(rt1.getReferenceName());
            Class c2 = SymbolTable.getInstance().getClassOrInterface(rt2.getReferenceName());

            return c1.isDescendantOf(c2);
        }

        //We never get to this case.
        return false;
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
