package exceptions.semantical.sentence;

import symboltable.types.Type;
import token.Token;

public class TypesDontConformException extends SentenceException{
    protected Type t1;
    protected Type t2;
    public TypesDontConformException(Token t, Type t1, Type t2) {
        super(t);
        this.t1 = t1;
        this.t2 = t2;
    }

    @Override
    protected String getSpecificMessage() {
        return "Error: the types " + t1 + " and " + t2 + "(used in line " + lineNumber + ") don't conform.";
    }
}
