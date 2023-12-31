package symboltable.ast.sentencenodes;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import exceptions.semantical.declaration.GenericsException;
import exceptions.semantical.sentence.LocalVarAndParameterShareNameException;
import exceptions.semantical.sentence.LocalVariableInitializedWithNullException;
import exceptions.semantical.sentence.VoidInTypedExpressionException;
import symboltable.ast.expressionnodes.ExpressionNode;
import symboltable.symbols.members.Variable;
import symboltable.types.ReferenceType;
import symboltable.types.Type;

public class LocalVariableNode extends SentenceNode {
    public static int classID = 0;
    private final int id;
    protected ExpressionNode expression;
    protected Variable variable;

    public LocalVariableNode() {
        super();
        id = classID;
        classID++;
    }

    @Override
    protected int getID() {
        return id;
    }
    public ExpressionNode getExpression() {
        return expression;
    }
    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    @Override
    protected void checkSelf() throws CompilerException {
        Type expressionType = expression.check();

        if(Type.isVoid(expressionType))
            throw new VoidInTypedExpressionException(token);

        if(Type.isNull(expressionType))
            throw new LocalVariableInitializedWithNullException(token);

        if(sharesNameWithParameter())
            throw new LocalVarAndParameterShareNameException(token);

        if(Type.isReferenceType(expressionType)) {
            ReferenceType rt = (ReferenceType) expressionType;
            if(rt.usesDiamondNotation()) {
                String error = "Error in line " + token.getLineNumber() + " can't infer generic types in that context.";
                throw new GenericsException(token, error);
            }
        }

        variable.setType(expressionType);
    }

    private boolean sharesNameWithParameter() {
        return contextUnit.getParameter(token.getLexeme()) != null;
    }

    @Override
    public void generate() throws CompilerException {
        expression.generate();

        int offset = variable.getOffset();
        String c = " # We store what's on top of the pile in the stack through localvar's offset";
        CodeGenerator.getInstance().append("STORE " + offset + c);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("var ");
        sb.append(token.getLexeme());
        sb.append("\n");
        sb.append(tabs());
        sb.append("expression");

        if(expression != null) {
            if(variable != null) {
                sb.append(" (type ");
                sb.append(variable.getType());
                sb.append(")");
            }

            sb.append(": \n");
            expression.stringDepth = stringDepth+1;
            sb.append(expression);
        } else {
            sb.append(": \n");
            sb.append(tabs());
            sb.append("[null]");
        }

        sb.append("\n");

        return sb.toString();
    }
}
