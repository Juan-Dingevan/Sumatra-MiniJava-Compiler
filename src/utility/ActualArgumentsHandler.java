package utility;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.ParameterArityMismatchException;
import exceptions.semantical.sentence.SentenceException;
import exceptions.semantical.sentence.TypesDontConformException;
import symboltable.ast.expressionnodes.ExpressionNode;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.members.Parameter;
import symboltable.symbols.members.Unit;
import symboltable.table.SymbolTable;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import token.Token;

import java.util.List;

public abstract class ActualArgumentsHandler {

    public static void checkActualArguments(Unit u, List<ExpressionNode> actualArgs, Token callerToken, ReferenceType callerType) throws CompilerException {
        List<Parameter> formalArgs = u.getParameters();

        int formalArgsArity = formalArgs.size();
        int actualArgsArity = actualArgs.size();

        if(formalArgsArity != actualArgsArity)
            throw new ParameterArityMismatchException(callerToken, formalArgsArity, actualArgsArity);

        ConcreteClass referencedClass = SymbolTable.getInstance().getClass(callerType.getReferenceName());

        for(int i = 0; i < formalArgsArity; i++) {
            Parameter formalArg = formalArgs.get(i);
            ExpressionNode actualArg = actualArgs.get(i);

            Type expectedType = formalArg.getType();
            Type gotType = actualArg.check();

            if(Type.isReferenceType(expectedType)) {
                ReferenceType expectedReferenceType = (ReferenceType) expectedType;
                String reference = expectedReferenceType.getReferenceName();

                if(referencedClass.isGenericType(reference)) {
                    List<String> genericDeclaration = referencedClass.getGenericTypes();
                    List<String> genericInstantiation = callerType.getGenericTypes();

                    int index = genericDeclaration.indexOf(reference);

                    String realReference = genericInstantiation.get(index);
                    ReferenceType instantiatedExpectedType = new ReferenceType(realReference);

                    expectedType = instantiatedExpectedType;
                }
            }

            if(!Type.typesConformInContext(gotType, expectedType, referencedClass)) {
                checkDiamondNotation(callerToken, expectedType, gotType);
            }
        }
    }

    private static void checkDiamondNotation(Token callerToken, Type expectedType, Type gotType) throws TypesDontConformException {
        boolean lhsIsReferenceType = Type.isReferenceType(expectedType);
        boolean rhsIsReferenceType = Type.isReferenceType(gotType);
        boolean bothSidesAreReferenceType = lhsIsReferenceType && rhsIsReferenceType;

        if(bothSidesAreReferenceType) {
            ReferenceType lhsReferenceType = (ReferenceType) expectedType;
            ReferenceType rhsReferenceType = (ReferenceType) gotType;

            boolean rhsDiamondNotation = rhsReferenceType.usesDiamondNotation();
            boolean lhsHasGenericTypes = lhsReferenceType.hasGenericTypes();

            boolean diamondCanBeInferred = rhsDiamondNotation &&
                    lhsHasGenericTypes;

            if(diamondCanBeInferred) {
                rhsReferenceType.setGenericTypes(lhsReferenceType.getGenericTypes());
            } else {
                throw new TypesDontConformException(callerToken, expectedType, gotType);
            }
        } else {
            throw new TypesDontConformException(callerToken, expectedType, gotType);
        }
    }
}
