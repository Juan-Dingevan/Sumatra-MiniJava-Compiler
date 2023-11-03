package symboltable.symbols.members;

import codegenerator.CodeGenerator;
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

        int staticBonus = isStatic() ? 0 : 1;
        int numberOfParameters = parameterList.size();
        int minOffset = PARAMETER_MIN_OFFSET + staticBonus;
        for(int i = 1; i <= numberOfParameters; i++) {
            int offset = numberOfParameters - i + minOffset;
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
        ast.giveLocalVariablesOffset();
    }

    public void generate() throws CompilerException {
        String tag = getTag();

        String c1 = " # We store the dynamic link";
        CodeGenerator.getInstance().append(tag + ": LOADFP" + c1);

        String c2 = " # We signal were the AR starts";
        CodeGenerator.getInstance().append("LOADSP" + c2);

        String c3 = " # We signal the AR being built as the current AR";
        CodeGenerator.getInstance().append("STOREFP" + c3);

        ast.generate();

        String c4 = " # We point FP to caller's AR";
        CodeGenerator.getInstance().append("STOREFP" + c4);

        int dynamicExtraCell = isStatic() ? 0 : 1;
        int n = parameterList.size() + dynamicExtraCell;

        String c5 = " # We free up memory cells equal to number of params [+1 if unit is dynamic]";
        CodeGenerator.getInstance().append("RET " + n + c5);
    }

    public abstract String getTag();
}
