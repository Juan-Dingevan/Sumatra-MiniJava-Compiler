package exceptions.semantical.sentence;

import exceptions.semantical.SemanticException;
import symboltable.ast.expressionnodes.ExpressionNode;
import token.Token;

public class InvalidExpressionAsSentenceException extends SemanticException {
    private ExpressionNode expression;
    public InvalidExpressionAsSentenceException(Token t, ExpressionNode expression) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "The expression " + expression.getDeclarationForm() + " (declared in line " + lineNumber + ") is not valid as a sentence.";
    }
}
