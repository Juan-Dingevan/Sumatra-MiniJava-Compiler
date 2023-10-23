package symboltable.types;

import symboltable.ast.expressionnodes.accesses.ConstructorAccessNode;

import java.util.ArrayList;
import java.util.List;

public class ReferenceType extends Type{
    String referenceName;
    protected List<String> genericTypes;

    public ReferenceType(String referenceName) {
        this.referenceName = referenceName;
        genericTypes = new ArrayList<>();
    }

    public boolean usesDiamondNotation() {
        return genericTypes == ConstructorAccessNode.DIAMOND_NOTATION;
    }

    public boolean hasGenericTypes() {
        boolean hasGenericTypes = genericTypes != ConstructorAccessNode.NO_GENERIC_TYPES;
        boolean sizeGreaterThanZero = genericTypes.size() > 0;
        return hasGenericTypes && sizeGreaterThanZero;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public String toString() {
        String s = "referenceType(" + referenceName + ")";

        if(genericTypes.size() > 0)
            s += "<";

        for(String g : genericTypes)
            s += g + ", ";

        if(genericTypes.size() > 0) {
            s = s.substring(0, s.length()-2);
            s += ">";
        }

        return s;
    }

    public List<String> getGenericTypes() {
        return genericTypes;
    }
    public void setGenericTypes(List<String> genericTypes) {
        this.genericTypes = genericTypes;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ReferenceType) {
            ReferenceType rt = (ReferenceType) obj;
            return referenceName.equals(rt.getReferenceName());
        }

        return false;
    }
}
