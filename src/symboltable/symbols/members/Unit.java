package symboltable.symbols.members;

import exceptions.general.CompilerException;
import exceptions.semantical.declaration.ParameterAlreadyExistsException;
import symboltable.ast.sentencenodes.BlockNode;
import symboltable.symbols.classes.Class;
import token.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Unit extends Member{
    public static boolean isConstructor(Unit u){
        return u instanceof Constructor;
    }

    protected HashMap<String, Parameter> parameterMap;
    protected List<Parameter> parameterList;
    protected BlockNode ast;
    public Unit(Token t, Class memberOf) {
        super(t, memberOf);
        parameterMap = new HashMap<>();
        parameterList = new ArrayList<>();
    }

    public abstract boolean isStatic();

    public void checkDeclaration() throws CompilerException {
        for(Parameter p : parameterMap.values())
            p.checkDeclaration();

        int numberOfParameters = parameterList.size();
        for(int i = 1; i <= numberOfParameters; i++) {
            int offset = numberOfParameters - i + PARAMETER_MIN_OFFSET;
            int index = i-1;
            parameterList.get(index).setOffset(offset);
        }
    }

    public int getReturnOffset() {
        return PARAMETER_MIN_OFFSET + parameterList.size();
    }

    public boolean exists(Parameter p) {
        return parameterMap.get(p.getName()) != null;
    }

    public Parameter getParameter(String name) {
        return parameterMap.get(name);
    }

    public void addParameter(Parameter p) throws CompilerException {
        if(!exists(p)) {
            parameterMap.put(p.getName(), p);
            parameterList.add(p);
        } else
            throw new ParameterAlreadyExistsException(p.getToken());
    }
    public List<Parameter> getParameters() {
        return parameterList;
    }

    public void setAST(BlockNode ast) {
        this.ast = ast;
    }
    public BlockNode getAST() {
        return ast;
    }

    public void checkSentences() throws CompilerException {
        ast.check();
    }
}
