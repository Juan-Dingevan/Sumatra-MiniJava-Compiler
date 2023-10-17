package utility;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.ParameterArityMismatchException;
import exceptions.semantical.sentence.SentenceException;
import exceptions.semantical.sentence.TypesDontConformException;
import symboltable.ast.expressionnodes.ExpressionNode;
import symboltable.symbols.members.Parameter;
import symboltable.symbols.members.Unit;
import symboltable.types.Type;

import java.util.List;

public abstract class ActualArgumentsHandler {
    public static void checkActualArguments(Unit u, List<ExpressionNode> actualArgs) throws CompilerException {
        List<Parameter> formalArgs = u.getParameters();

        int formalArgsArity = formalArgs.size();
        int actualArgsArity = actualArgs.size();

        if(formalArgsArity != actualArgsArity)
            throw new ParameterArityMismatchException(u.getToken(), formalArgsArity, actualArgsArity);

        for(int i = 0; i < formalArgsArity; i++) {
            Parameter formalArg = formalArgs.get(i);
            ExpressionNode actualArg = actualArgs.get(i);

            Type expectedType = formalArg.getType();
            Type gotType = actualArg.check();

            if(!Type.typesConform(gotType, expectedType))
                throw new TypesDontConformException(u.getToken(), expectedType, gotType);
        }
    }
}
