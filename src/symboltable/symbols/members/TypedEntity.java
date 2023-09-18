package symboltable.symbols.members;

import symboltable.types.Type;
import token.Token;

public abstract class TypedEntity extends Member{
    protected Type type;
    public TypedEntity(Token t) {
        super(t);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
