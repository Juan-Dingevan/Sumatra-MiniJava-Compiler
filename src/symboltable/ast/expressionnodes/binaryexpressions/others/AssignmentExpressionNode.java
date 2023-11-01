package symboltable.ast.expressionnodes.binaryexpressions.others;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.NonAssignableExpressionException;
import exceptions.semantical.sentence.TypesDontConformException;
import symboltable.ast.expressionnodes.BinaryExpressionNode;
import symboltable.ast.expressionnodes.accesses.VariableAccessNode;
import symboltable.symbols.members.Variable;
import symboltable.types.ReferenceType;
import symboltable.types.Type;

public class AssignmentExpressionNode extends BinaryExpressionNode {
    @Override
    public void generate() throws CompilerException {
        //TODO implement!
    }
    @Override
    public boolean isAssignment() {
        return true;
    }

    @Override
    public Type check() throws CompilerException {
        Type lhsType = lhs.check();
        Type rhsType = rhs.check();

        if(!lhs.canBeAssigned())
            throw new NonAssignableExpressionException(token);

        if(!Type.typesConformInContext(rhsType, lhsType, contextClass)) {
            checkDiamondNotation(lhsType, rhsType);
        }

        return lhsType;
    }

    private void checkDiamondNotation(Type lhsType, Type rhsType) throws TypesDontConformException {
        boolean lhsIsReferenceType = Type.isReferenceType(lhsType);
        boolean rhsIsReferenceType = Type.isReferenceType(rhsType);
        boolean bothSidesAreReferenceType = lhsIsReferenceType && rhsIsReferenceType;

        if(bothSidesAreReferenceType) {
            ReferenceType lhsReferenceType = (ReferenceType) lhsType;
            ReferenceType rhsReferenceType = (ReferenceType) rhsType;

            boolean rhsDiamondNotation = rhsReferenceType.usesDiamondNotation();
            boolean lhsHasGenericTypes = lhsReferenceType.hasGenericTypes();

            boolean lhsIsVariable = lhs instanceof VariableAccessNode;
            boolean lhsHasChaining;

            VariableAccessNode van;

            if(lhsIsVariable) {
                van = (VariableAccessNode) lhs;
                lhsHasChaining = van.hasChaining();
            } else {
                lhsHasChaining = true;
            }

            boolean diamondCanBeInferred = rhsDiamondNotation &&
                                           lhsHasGenericTypes &&
                                           lhsIsVariable &&
                                           !lhsHasChaining;

            if(diamondCanBeInferred) {
                rhsReferenceType.setGenericTypes(lhsReferenceType.getGenericTypes());
            } else {
                throw new TypesDontConformException(token, lhsType, rhsType);
            }
        } else {
            throw new TypesDontConformException(token, lhsType, rhsType);
        }
    }
}
