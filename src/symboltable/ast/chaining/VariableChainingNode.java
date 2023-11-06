package symboltable.ast.chaining;

import codegenerator.CodeGenerator;
import exceptions.general.CompilerException;
import exceptions.semantical.sentence.DynamicUsageInStaticContextException;
import exceptions.semantical.sentence.PrimitiveTypeHasChainingException;
import exceptions.semantical.sentence.PrivateMemberAccessException;
import exceptions.semantical.sentence.UnresolvedNameException;
import symboltable.privacy.Privacy;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.members.Attribute;
import symboltable.table.SymbolTable;
import symboltable.types.ReferenceType;
import symboltable.types.Type;
import token.Token;
import token.TokenType;

import java.util.List;

public class VariableChainingNode extends ChainingNode {
    protected Attribute attribute;
    @Override
    protected void selfGenerate() throws CompilerException{
        if(attribute.isStatic())
            generateStaticAccess();
        else
            generateDynamicAccess();
    }

    private void generateStaticAccess() throws CompilerException {
        //TODO: se extiende el "consultar" de VariableAccessNode
        boolean readAccess = !isAssignmentLHS || hasChaining();
        String tag = CodeGenerator.getAttributeTag(attribute);

        String cPush = " # We put the static attribute's tag at the top of the stack";
        CodeGenerator.getInstance().append("PUSH " + tag + cPush);

        if(readAccess) {
            String cRead = " # We read the attributes value from the data using the tag";
            CodeGenerator.getInstance().append("LOADREF 0" + cRead);
        } else {
            String cSwap = " # We swap the tag and what we desire to write into the attribute";
            CodeGenerator.getInstance().append("SWAP" + cSwap);

            String cWrite = " # We write into the data through the tag";
            CodeGenerator.getInstance().append("STOREREF 0" + cWrite);
        }
    }

    private void generateDynamicAccess() throws CompilerException {
        boolean readAccess = !isAssignmentLHS || hasChaining();
        int offset = attribute.getOffset();

        //we assume that the 'this' reference for the "chainee' is on top of the stack

        if(readAccess) {
            String cRead = " # We get the variable from the heap through the 'this' reference and its offset";
            CodeGenerator.getInstance().append("LOADREF " + offset + cRead);
        } else {
            String cSwap = " # We swap the 'this' reference and what we desire to write into the attribute";
            CodeGenerator.getInstance().append("SWAP" + cSwap);

            String cWrite = " # We write into the heap through the 'this' reference and the offset";
            CodeGenerator.getInstance().append("STOREREF " + offset + cWrite);
        }
    }

    @Override
    protected boolean selfCanBeAssigned() {
        return true;
    }

    @Override
    protected Type checkSelf(Type callerType, Token callerToken) throws CompilerException {
        if(!Type.isReferenceType(callerType))
            throw new PrimitiveTypeHasChainingException(token, callerType);

        ReferenceType rt = (ReferenceType) callerType;

        String className = rt.getReferenceName();
        String variableName = token.getLexeme();

        ConcreteClass referencedClass = SymbolTable.getInstance().getClass(className);
        attribute = referencedClass.getAttribute(variableName);

        if(attribute == null)
            throw new UnresolvedNameException(token, referencedClass.getToken());

        Privacy privacy = attribute.getPrivacy();

        boolean accessedFromDeclaringClass = contextClass == attribute.getMemberOf();
        boolean isPrivate = privacy != Privacy.publicS;

        if(isPrivate && !accessedFromDeclaringClass) {
            throw new PrivateMemberAccessException(token);
        }

        Type returnType = attribute.getType();

        if(Type.isReferenceType(returnType)){
            ReferenceType possibleReturnReferenceType = (ReferenceType) returnType;
            String reference = possibleReturnReferenceType.getReferenceName();

            if(referencedClass.isGenericType(reference)) {
                List<String> genericDeclaration = referencedClass.getGenericTypes();
                List<String> genericInstantiation = rt.getGenericTypes();

                int index = genericDeclaration.indexOf(reference);

                if(genericInstantiation.size() > 0) {
                    String realReference = genericInstantiation.get(index);
                    ReferenceType realReturnType = new ReferenceType(realReference);
                    returnType = realReturnType;
                }
            }
        }

        return returnType;
    }

    @Override
    public boolean isCall() {
        if(hasChaining())
            return chainingNode.isCall();
        else
            return false;
    }


}
