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

    protected HashMap<String, Parameter> parameterMap;
    protected List<Parameter> parameterList;
    protected BlockNode ast;
    public Unit(Token t, Class memberOf) {
        super(t, memberOf);
        parameterMap = new HashMap<>();
        parameterList = new ArrayList<>();
    }

    public void checkDeclaration() throws CompilerException {
        for(Parameter p : parameterMap.values())
            p.checkDeclaration();
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
