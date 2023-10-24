package symboltable.ast.expressionnodes.binaryexpressions.numtonum;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.InvalidTypesInAddException;
import symboltable.ast.expressionnodes.binaryexpressions.NumberToNumberBinaryExpressionNode;
import symboltable.types.*;

public class AddExpressionNode extends NumberToNumberBinaryExpressionNode {
    @Override
    public Type check() throws CompilerException {
        Type lhsType = lhs.check();
        Type rhsType = rhs.check();

        boolean lhsNumber = Type.isNumber(lhsType);
        boolean rhsNumber = Type.isNumber(rhsType);

        boolean lhsFloat = lhsType.equals(new SFloat());
        boolean rhsFloat = rhsType.equals(new SFloat());

        boolean lhsString = isString(lhsType);
        boolean rhsString = isString(rhsType);

        boolean atLeastOneNumber = lhsNumber || rhsNumber;
        boolean atLeastOneFloat  = lhsFloat  || rhsFloat;
        boolean atLeastOneString = lhsString || rhsString;

        boolean bothNumbers = lhsNumber && rhsNumber;
        boolean stringCoercion = atLeastOneString && atLeastOneNumber;

        boolean validTypes = bothNumbers || stringCoercion;

        if(!validTypes) {
            throw new InvalidTypesInAddException(token);
        }

        return getWidestType(atLeastOneFloat, atLeastOneString);
    }

    private Type getWidestType(boolean floats, boolean strings) {
        if(strings)
            return new ReferenceType("String");

        if(floats)
            return new SFloat();

        return new Int();
    }

    private boolean isString(Type t) {
        if(Type.isReferenceType(t)) {
            ReferenceType rt = (ReferenceType) t;
            return rt.getReferenceName().equals("String");
        }

        return false;
    }
}
