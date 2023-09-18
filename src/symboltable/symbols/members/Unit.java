package symboltable.symbols.members;

import exceptions.general.CompilerException;
import exceptions.semantical.ParameterAlreadyExistsException;
import token.Token;

import java.util.HashMap;

public abstract class Unit extends Member{

    protected HashMap<String, Parameter> parameters;
    public Unit(Token t) {
        super(t);
        parameters = new HashMap<>();
    }

    protected boolean exists(Parameter p) {
        return parameters.get(p.getName()) != null;
    }

    public void addParameter(Parameter p) throws CompilerException {
        if(!exists(p))
            parameters.put(p.getName(), p);
        else
            throw new ParameterAlreadyExistsException(p.getToken());
    }
}
