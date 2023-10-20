package exceptions.semantical.sentence;

import token.Token;

public class DynamicUsageInStaticContextException extends SentenceException{
    public DynamicUsageInStaticContextException(Token t) {
        super(t);
    }

    @Override
    protected String getSpecificMessage() {
        return "The member " + lexeme + " accessed in line " + lineNumber + " is dynamic, and can't be accessed in a static context.";
    }
}
