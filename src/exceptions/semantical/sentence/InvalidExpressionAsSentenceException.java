package exceptions.semantical.sentence;

import exceptions.semantical.SemanticException;
import symboltable.ast.expressionnodes.ExpressionNode;
import token.Token;

public class InvalidExpressionAsSentenceException extends SemanticException {
    public InvalidExpressionAsSentenceException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "The expression declared in line " + lineNumber + " is not valid as a sentence.";
    }
}
