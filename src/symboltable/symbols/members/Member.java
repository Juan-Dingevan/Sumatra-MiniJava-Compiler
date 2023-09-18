package symboltable.symbols.members;

import symboltable.privacy.Privacy;
import symboltable.symbols.Symbol;
import token.Token;

public abstract class Member extends Symbol {
    protected Privacy privacy;

    public Member(Token t) {
        super(t);
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy p) {
        privacy = p;
    }

}
