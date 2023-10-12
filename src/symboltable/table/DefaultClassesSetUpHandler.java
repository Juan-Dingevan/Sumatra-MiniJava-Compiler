package symboltable.table;

import exceptions.general.CompilerException;
import symboltable.ast.sentencenodes.defaultmethodsblocks.*;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.members.Constructor;
import symboltable.symbols.members.Method;
import symboltable.symbols.members.Parameter;
import symboltable.types.Char;
import symboltable.types.Int;
import symboltable.types.ReferenceType;
import symboltable.types.SBoolean;
import symboltable.types.Void;
import token.TokenConstants;

import static symboltable.privacy.Privacy.pub;
import static token.TokenConstants.*;

public abstract class DefaultClassesSetUpHandler {
    public static ConcreteClass getObject() {
        try {
            ConcreteClass object = new ConcreteClass(OBJECT_TOKEN);

            Parameter iDebugPrint = new Parameter(token.TokenConstants.PARAMETER_I_TOKEN, object);
            iDebugPrint.setType(new Int());

            Method debugPrint = new Method(DEBUG_PRINT_TOKEN, object);
            debugPrint.setStatic(true);
            debugPrint.setPrivacy(pub);
            debugPrint.setReturnType(new Void());
            debugPrint.addParameter(iDebugPrint);
            debugPrint.setAST(new DebugPrintBlockNode());

            object.addMethod(debugPrint);

            object.setConstructor(Constructor.getDefaultConstructorForClass(object));

            return object;
        } catch(CompilerException ex) {
            return null;
        }
    }

    public static ConcreteClass getSystem() {
        try {
            ConcreteClass system = new ConcreteClass(SYSTEM_TOKEN);

            Parameter b = new Parameter(token.TokenConstants.PARAMETER_B_TOKEN, system);
            b.setType(new SBoolean());

            Parameter c = new Parameter(token.TokenConstants.PARAMETER_C_TOKEN, system);
            c.setType(new Char());

            Parameter i = new Parameter(token.TokenConstants.PARAMETER_I_TOKEN, system);
            i.setType(new Int());

            Parameter s = new Parameter(token.TokenConstants.PARAMETER_S_TOKEN, system);
            s.setType(new ReferenceType("String"));

            Parameter bln = new Parameter(token.TokenConstants.PARAMETER_B_TOKEN, system);
            bln.setType(new SBoolean());

            Parameter cln = new Parameter(token.TokenConstants.PARAMETER_C_TOKEN, system);
            cln.setType(new Char());

            Parameter iln = new Parameter(token.TokenConstants.PARAMETER_I_TOKEN, system);
            iln.setType(new Int());

            Parameter sln = new Parameter(token.TokenConstants.PARAMETER_S_TOKEN, system);
            sln.setType(new ReferenceType("String"));

            Method read = new Method(TokenConstants.READ_TOKEN, system);
            read.setStatic(true);
            read.setPrivacy(pub);
            read.setReturnType(new Int());
            read.setAST(new ReadBlockNode());

            Method printB = new Method(TokenConstants.PRINTB_TOKEN, system);
            printB.setStatic(true);
            printB.setPrivacy(pub);
            printB.setReturnType(new Void());
            printB.addParameter(b);
            printB.setAST(new PrintBooleanBlockNode());

            Method printC = new Method(TokenConstants.PRINTC_TOKEN, system);
            printC.setStatic(true);
            printC.setPrivacy(pub);
            printC.setReturnType(new Void());
            printC.addParameter(c);
            printC.setAST(new PrintCharBlockNode());

            Method printI = new Method(TokenConstants.PRINTI_TOKEN, system);
            printI.setStatic(true);
            printI.setPrivacy(pub);
            printI.setReturnType(new Void());
            printI.addParameter(i);
            printI.setAST(new PrintIntBlockNode());

            Method printS = new Method(TokenConstants.PRINTS_TOKEN, system);
            printS.setStatic(true);
            printS.setPrivacy(pub);
            printS.setReturnType(new Void());
            printS.addParameter(s);
            printS.setAST(new PrintStringBlockNode());

            Method println = new Method(TokenConstants.PRINTLN_TOKEN, system);
            println.setStatic(true);
            println.setPrivacy(pub);
            println.setReturnType(new Void());
            println.setAST(new PrintLineBlockNode());

            Method printBln = new Method(TokenConstants.PRINTBLN_TOKEN, system);
            printBln.setStatic(true);
            printBln.setPrivacy(pub);
            printBln.setReturnType(new Void());
            printBln.addParameter(bln);
            printBln.setAST(new PrintLineBooleanBlockNode());

            Method printCln = new Method(TokenConstants.PRINTCLN_TOKEN, system);
            printCln.setStatic(true);
            printCln.setPrivacy(pub);
            printCln.setReturnType(new Void());
            printCln.addParameter(cln);
            printCln.setAST(new PrintLineCharBlockNode());

            Method printIln = new Method(TokenConstants.PRINTILN_TOKEN, system);
            printIln.setStatic(true);
            printIln.setPrivacy(pub);
            printIln.setReturnType(new Void());
            printIln.addParameter(iln);
            printIln.setAST(new PrintLineIntBlockNode());

            Method printSln = new Method(TokenConstants.PRINTSLN_TOKEN, system);
            printSln.setStatic(true);
            printSln.setPrivacy(pub);
            printSln.setReturnType(new Void());
            printSln.addParameter(sln);
            printSln.setAST(new PrintLineStringBlockNode());

            system.setInheritance(OBJECT_TOKEN);
            system.addMethod(read);
            system.addMethod(printB);
            system.addMethod(printC);
            system.addMethod(printI);
            system.addMethod(printS);
            system.addMethod(println);
            system.addMethod(printBln);
            system.addMethod(printCln);
            system.addMethod(printIln);
            system.addMethod(printSln);

            return system;
        } catch (CompilerException e) {
            //this will never happen
            return null;
        }
    }

    public static ConcreteClass getString() {
        ConcreteClass string = new ConcreteClass(STRING_TOKEN);
        string.setInheritance(OBJECT_TOKEN);

        return string;
    }

}
