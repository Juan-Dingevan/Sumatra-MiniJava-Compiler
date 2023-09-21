package symboltable.symbols.members;

import exceptions.general.CompilerException;
import exceptions.semantical.ParameterAlreadyExistsException;
import symboltable.symbols.classes.Class;
import token.Token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public abstract class Unit extends Member{

    protected HashMap<String, Parameter> parameterMap;
    protected List<Parameter> parameterList;
    public Unit(Token t, Class memberOf) {
        super(t, memberOf);
        parameterMap = new HashMap<>();
        parameterList = new ArrayList<>();
    }

    public void checkDeclaration() throws CompilerException {
        for(Parameter p : parameterMap.values())
            p.checkDeclaration();
    }

    protected boolean exists(Parameter p) {
        return parameterMap.get(p.getName()) != null;
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
}
