package symboltable.symbols.members;

import symboltable.privacy.Privacy;
import symboltable.symbols.Symbol;
import symboltable.symbols.classes.Class;
import token.Token;

public abstract class Member extends Symbol {
    protected Privacy privacy;
    protected Class memberOf;

    public Member(Token t, Class memberOf) {
        super(t);
        this.memberOf = memberOf;
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

}
