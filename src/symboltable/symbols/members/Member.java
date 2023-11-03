package symboltable.symbols.members;

import symboltable.privacy.Privacy;
import symboltable.symbols.Symbol;
import symboltable.symbols.classes.Class;
import token.Token;

public abstract class Member extends Symbol {
    public static final int DEFAULT_OFFSET = Integer.MIN_VALUE;
    public static final int PARAMETER_MIN_OFFSET = 3;
    public static final int METHOD_MIN_OFFSET = 0;
    public static final int ATTRIBUTE_MIN_OFFSET = 1;
    public static final int LOCAL_VAR_MIN_OFFSET = 0;
    protected Privacy privacy;
    protected Class memberOf;
    protected int offset;

    public Member(Token t, Class memberOf) {
        super(t);
        this.memberOf = memberOf;
        offset = DEFAULT_OFFSET;
    }

    public Class getMemberOf() {
        return memberOf;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy p) {
        privacy = p;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
