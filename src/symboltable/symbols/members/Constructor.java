package symboltable.symbols.members;

import symboltable.ast.sentencenodes.BlockNode;
import symboltable.privacy.Privacy;
import symboltable.symbols.classes.Class;
import symboltable.symbols.classes.ConcreteClass;
import token.Token;
import token.TokenType;
import utility.StringUtilities;

public class Constructor extends Unit {
    private static final int LEVEL = 2;

    public static Constructor getDefaultConstructorForClass(ConcreteClass c) {
        Constructor constructor = new Constructor(c.getToken(), c);
        constructor.setPrivacy(Privacy.pub);

        BlockNode ast = new BlockNode();
        ast.setToken(new Token(TokenType.punctuation_open_curly, "{", c.getToken().getLineNumber()));
        ast.setParentBlock(BlockNode.NULL_PARENT);
        constructor.setAST(ast);

        return constructor;
    }

    public Constructor(Token t, Class memberOf) {
        super(t, memberOf);
    }

    public String toString() {
        String prefix = StringUtilities.getDashesForDepth(LEVEL);
        String s = prefix + "CONSTRUCTOR PRIVACY:" + privacy + "\n";

        s += prefix + "PARAMETERS:\n";

        for(Parameter p : parameterMap.values())
            s += p.toString() + "\n";

        return s;
    }
}
