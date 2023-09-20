package symboltable.table;

import exceptions.general.CompilerException;
import symboltable.symbols.classes.ConcreteClass;
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
            Parameter iDebugPrint = new Parameter(token.TokenConstants.PARAMETER_I_TOKEN);
            iDebugPrint.setType(new Int());

            Method debugPrint = new Method(DEBUG_PRINT_TOKEN);
            debugPrint.setStatic(true);
            debugPrint.setPrivacy(pub);
            debugPrint.setReturnType(new Void());
            debugPrint.addParameter(iDebugPrint);

            ConcreteClass object = new ConcreteClass(OBJECT_TOKEN);
            object.addMethod(debugPrint);

            return object;
        } catch(CompilerException ex) {
            return null;
        }
    }

    public static ConcreteClass getSystem() {
        try {
            Parameter b = new Parameter(token.TokenConstants.PARAMETER_B_TOKEN);
            b.setType(new SBoolean());

            Parameter c = new Parameter(token.TokenConstants.PARAMETER_C_TOKEN);
            c.setType(new Char());

            Parameter i = new Parameter(token.TokenConstants.PARAMETER_I_TOKEN);
            i.setType(new Int());

            Parameter s = new Parameter(token.TokenConstants.PARAMETER_S_TOKEN);
            s.setType(new ReferenceType("String"));

            Parameter bln = new Parameter(token.TokenConstants.PARAMETER_B_TOKEN);
            bln.setType(new SBoolean());

            Parameter cln = new Parameter(token.TokenConstants.PARAMETER_C_TOKEN);
            cln.setType(new Char());

            Parameter iln = new Parameter(token.TokenConstants.PARAMETER_I_TOKEN);
            iln.setType(new Int());

            Parameter sln = new Parameter(token.TokenConstants.PARAMETER_S_TOKEN);
            sln.setType(new ReferenceType("String"));

            Method read = new Method(TokenConstants.READ_TOKEN);
            read.setStatic(true);
            read.setPrivacy(pub);
            read.setReturnType(new Int());

            Method printB = new Method(TokenConstants.PRINTB_TOKEN);
            printB.setStatic(true);
            printB.setPrivacy(pub);
            printB.setReturnType(new Void());
            printB.addParameter(b);

            Method printC = new Method(TokenConstants.PRINTC_TOKEN);
            printC.setStatic(true);
            printC.setPrivacy(pub);
            printC.setReturnType(new Void());
            printC.addParameter(c);

            Method printI = new Method(TokenConstants.PRINTI_TOKEN);
            printI.setStatic(true);
            printI.setPrivacy(pub);
            printI.setReturnType(new Void());
            printI.addParameter(i);

            Method printS = new Method(TokenConstants.PRINTS_TOKEN);
            printS.setStatic(true);
            printS.setPrivacy(pub);
            printS.setReturnType(new Void());
            printS.addParameter(s);

            Method println = new Method(TokenConstants.PRINTLN_TOKEN);
            println.setStatic(true);
            println.setPrivacy(pub);
            println.setReturnType(new Void());

            Method printBln = new Method(TokenConstants.PRINTBLN_TOKEN);
            printBln.setStatic(true);
            printBln.setPrivacy(pub);
            printBln.setReturnType(new Void());
            printBln.addParameter(bln);

            Method printCln = new Method(TokenConstants.PRINTCLN_TOKEN);
            printCln.setStatic(true);
            printCln.setPrivacy(pub);
            printCln.setReturnType(new Void());
            printCln.addParameter(cln);

            Method printIln = new Method(TokenConstants.PRINTILN_TOKEN);
            printIln.setStatic(true);
            printIln.setPrivacy(pub);
            printIln.setReturnType(new Void());
            printIln.addParameter(iln);

            Method printSln = new Method(TokenConstants.PRINTSLN_TOKEN);
            printSln.setStatic(true);
            printSln.setPrivacy(pub);
            printSln.setReturnType(new Void());
            printSln.addParameter(sln);

            ConcreteClass system = new ConcreteClass(SYSTEM_TOKEN);
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