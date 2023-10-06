package syntaxanalyzer;

import exceptions.general.CompilerException;
import exceptions.syntax.InvalidTokenFoundException;
import exceptions.syntax.TokenMismatchException;
import lexicalanalizer.LexicalAnalyzer;
import symboltable.ast.sentencenodes.BlockNode;
import symboltable.privacy.Privacy;
import symboltable.symbols.classes.Class;
import symboltable.symbols.members.Attribute;
import symboltable.symbols.members.Constructor;
import symboltable.symbols.members.Method;
import symboltable.symbols.members.Parameter;
import symboltable.types.*;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.classes.Interface;
import symboltable.table.SymbolTable;
import symboltable.types.Void;
import token.Token;
import token.TokenConstants;
import token.TokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static symboltable.privacy.Privacy.pub;

public class SyntaxAnalyzerImpl implements SyntaxAnalyzer {
    private static final boolean DEBUG = true;
    private final LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;

    public SyntaxAnalyzerImpl(LexicalAnalyzer lexicalAnalyzer) {
        this.lexicalAnalyzer = lexicalAnalyzer;
    }

    private void printIfDebug(String s) {
        if(DEBUG)
            System.out.print(s);
    }
    private void updateCurrentToken() throws CompilerException {
        currentToken = lexicalAnalyzer.getNextToken();
    }
    private boolean currentTokenIn(TokenType[] firsts) {
        TokenType tt = currentToken.getTokenType();
        return Arrays.asList(firsts).contains(tt);
    }

    private void match(TokenType tt) throws CompilerException {
        if(tt == currentToken.getTokenType()) {
            updateCurrentToken();
        } else {
            throw new TokenMismatchException(tt, currentToken);
        }
    }
    @Override
    public void analyze() throws CompilerException {
        updateCurrentToken();
        startingSymbol();
    }

    private void startingSymbol() throws CompilerException {
        printIfDebug("StartingSymbol");

        classList();
        Token eof = currentToken;

        match(TokenType.eof);

        SymbolTable.getInstance().setEOF(eof);

        printIfDebug("\n");
    }

    private void classList() throws CompilerException {
        printIfDebug("->ClassList");

        //RULE: <class_list> ::= <class> <class_list>
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_class, TokenType.reserved_word_interface})) {
            classNT();
            classList();
        }
        //RULE: <class_list> ::= epsilon
        //We do nothing
    }

    private void classNT() throws CompilerException {
        printIfDebug("->ClassNT");
        //RULE: <class> ::= <concrete_class>
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_class})) {
            concreteClass();
        }
        //RULE: <class> ::= <interface>
        else if (currentTokenIn(new TokenType[]{TokenType.reserved_word_interface})) {
            interfaceNT();
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {TokenType.reserved_word_class, TokenType.reserved_word_interface};
            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }
    }

    private void concreteClass() throws CompilerException {
        printIfDebug("->ConcreteClass");
        match(TokenType.reserved_word_class);

        Token classDeclarationToken = currentToken;

        match(TokenType.id_class);

        ConcreteClass c = new ConcreteClass(classDeclarationToken);
        SymbolTable.getInstance().addClass(c);

        List<String> genericTypes = optionalGenerics();
        SymbolTable.getInstance().getCurrentConcreteClass().setGenericTypes(genericTypes);

        optionalInheritance();
        match(TokenType.punctuation_open_curly);
        memberList();
        match(TokenType.punctuation_close_curly);
    }

    private void optionalInheritance() throws CompilerException {
        printIfDebug("->OptionalInheritance");
        //RULE: <optional_inheritance> ::= <inherits_from>
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_extends})) {
            inheritsFrom();
        }
        //RULE <optional_inheritance> ::= <implements>
        else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_implements})) {
            implementsNT();

            ConcreteClass c = SymbolTable.getInstance().getCurrentConcreteClass();
            c.setInheritance(TokenConstants.OBJECT_TOKEN);
        }
        //RULE <optional_inheritance> ::= epsilon
        else {
            ConcreteClass c = SymbolTable.getInstance().getCurrentConcreteClass();
            c.setInheritance(TokenConstants.OBJECT_TOKEN);
        }
    }

    private List<String> optionalGenerics() throws CompilerException {
        printIfDebug("->OptionalGenerics");

        List<String> genericTypes = new ArrayList<>();

        // RULE: <optional_generics> ::= < <generic_types_list> >
        if(currentTokenIn(new TokenType[]{TokenType.operand_lesser})) {
            generics(genericTypes);
        }

        // RULE: <optional_generics> ::= epsilon
        // We do nothing

        return genericTypes;
    }

    private void generics(List<String> genericTypes) throws CompilerException {
        printIfDebug("->Generics");

        match(TokenType.operand_lesser);
        genericTypesList(genericTypes);
        match(TokenType.operand_greater);
    }

    private void genericTypesList(List<String> genericTypes) throws CompilerException {
        printIfDebug("->GenericTypesList");

        Token declarationToken = currentToken;

        match(TokenType.id_class);

        genericTypes.add(declarationToken.getLexeme());

        genericTypesListSuccessor(genericTypes);
    }

    private void genericTypesListSuccessor(List<String> genericTypes) throws CompilerException {
        printIfDebug("->GenericTypesListSuccessor");

        // RULE <generic_types_list_successor> ::= , <generic_types_list>
        if(currentTokenIn(new TokenType[]{TokenType.punctuation_comma})) {
            match(TokenType.punctuation_comma);
            genericTypesList(genericTypes);
        }

        // RULE <generic_types_list_successor> ::= epsilon
        // We do nothing
    }

    private void inheritsFrom() throws CompilerException {
        printIfDebug("->InheritsFrom");
        match(TokenType.reserved_word_extends);

        Token inheritanceToken = currentToken;

        match(TokenType.id_class);

        List<String> parentDeclaredGenericTypes = optionalGenerics();

        ConcreteClass c = SymbolTable.getInstance().getCurrentConcreteClass();
        c.setInheritance(inheritanceToken);
        c.setParentDeclaredGenericTypes(parentDeclaredGenericTypes);
    }

    private void implementsNT() throws CompilerException {
        printIfDebug("->ImplementsNT");
        match(TokenType.reserved_word_implements);

        Token implementationToken = currentToken;

        match(TokenType.id_class);

        List<String> interfaceDeclaredGenericTypes = optionalGenerics();

        ConcreteClass c = SymbolTable.getInstance().getCurrentConcreteClass();
        c.setImplements(implementationToken);
        c.setInterfaceDeclaredGenericTypes(interfaceDeclaredGenericTypes);
    }

    private void memberList() throws CompilerException {
        printIfDebug("->MemberList");
        //RULE: <member_list> ::= <member><member_list>
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_static, TokenType.reserved_word_boolean,
                TokenType.reserved_word_char, TokenType.reserved_word_int, TokenType.reserved_word_float,
                TokenType.id_class, TokenType.reserved_word_void, TokenType.reserved_word_public, TokenType.reserved_word_private})) {

            member();
            memberList();
        }
        //RULE: <member_list> ::= epsilon
        //Do nothing
    }

    private void member() throws CompilerException {
        printIfDebug("->Member");

        Privacy p = optionalPrivacy();
        noPrivacyMember(p);
    }

    private void noPrivacyMember(Privacy currentMemberPrivacy) throws CompilerException {
        printIfDebug("->NoPrivacyMember");

        // RULE: <no_privacy_member> ::= id_class <member_id_class_successor>
        if(currentTokenIn(new TokenType[]{TokenType.id_class})) {
            Token idClassToken = currentToken;
            match(TokenType.id_class);
            memberIdClassSuccessor(currentMemberPrivacy, idClassToken);
        }
        else if (currentTokenIn(new TokenType[]{
                TokenType.reserved_word_static,
                TokenType.reserved_word_int,
                TokenType.reserved_word_char,
                TokenType.reserved_word_float,
                TokenType.reserved_word_boolean,
                TokenType.reserved_word_void
        })) {
            boolean staticity = optionalStatic();
            Type type = memberType(); //will never go to id_class.

            Token declarationToken = currentToken;

            match(TokenType.id_method_variable);

            attributeMethodSuccessor(currentMemberPrivacy, staticity, type, declarationToken);
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {TokenType.reserved_word_boolean, TokenType.reserved_word_char, TokenType.reserved_word_int, TokenType.reserved_word_float, TokenType.id_class, TokenType.reserved_word_void, TokenType.reserved_word_static};
            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }
    }

    private void memberIdClassSuccessor(Privacy currentMemberPrivacy, Token idClassToken) throws CompilerException {
        printIfDebug("->MemberIdClassSuccessor");
        // RULE: <member_id_class_successor> ::= <formal_arguments><block>
        // This rules captures the constructor.
        if(currentTokenIn(new TokenType[]{TokenType.punctuation_open_parenthesis})) {
            ConcreteClass currentClass = SymbolTable.getInstance().getCurrentConcreteClass();

            Constructor c = new Constructor(idClassToken, currentClass);
            c.setPrivacy(currentMemberPrivacy);

            currentClass.setConstructor(c);

            formalArguments();
            BlockNode block = block();

            c.setAST(block);
        }
        // RULE: <member_id_class_successor> ::= <optional_generics> id_method_variable <attribute_method_successor>
        // This rule captures methods that have a reference-type return type and attributes that are of a reference type and NOT static.
        else if (currentTokenIn(new TokenType[]{
                TokenType.operand_lesser,
                TokenType.id_method_variable
        })) {
            List<String> genericTypes = optionalGenerics();

            Token declarationToken = currentToken;
            ReferenceType type = new ReferenceType(idClassToken.getLexeme());
            type.setGenericTypes(genericTypes);

            match(TokenType.id_method_variable);

            attributeMethodSuccessor(currentMemberPrivacy, false, type, declarationToken);
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {TokenType.punctuation_open_parenthesis, TokenType.operand_lesser, TokenType.id_method_variable};
            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }
    }

    private Privacy optionalPrivacy() throws CompilerException {
        printIfDebug("->OptionalPrivacy");
        //RULE: <optional_privacy> ::= private
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_private})){
            match(TokenType.reserved_word_private);
            return Privacy.priv;
        }
        //RULE: <optional_privacy> ::= public
        else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_public})){
            match(TokenType.reserved_word_public);
            return pub;
        }

        //RULE: <optional_privacy> ::= epsilon
        //Since by default a member is public, we return public
        return pub;
    }

    private boolean optionalStatic() throws CompilerException {
        printIfDebug("->OptionalStatic");
        //RULE: <optional_static> ::= static
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_static})){
            match(TokenType.reserved_word_static);
            return true;
        }

        //RULE: <optional_static> ::= epsilon
        //Since by default a method/attribute is dynamic, we return false
        return false;
    }

    private Type memberType() throws CompilerException {
        printIfDebug("->MemberType");
        //RULE: <member_type> ::= <type>
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_boolean, TokenType.reserved_word_char, TokenType.reserved_word_int, TokenType.reserved_word_float, TokenType.id_class})) {
            return type();
        }
        //RULE: <member_type> ::= void
        else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_void})) {
            match(TokenType.reserved_word_void);
            return new Void();
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {TokenType.reserved_word_boolean, TokenType.reserved_word_char, TokenType.reserved_word_int, TokenType.reserved_word_float, TokenType.id_class, TokenType.reserved_word_void};
            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }
    }

    private Type type() throws CompilerException {
        printIfDebug("->Type");
        //RULE: <type> ::= <primitive_type>
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_boolean, TokenType.reserved_word_char, TokenType.reserved_word_int, TokenType.reserved_word_float})) {
            return primitiveType();
        } //RULE: <type> ::= id_class
        else if(currentTokenIn(new TokenType[]{TokenType.id_class})) {
            Token classIdToken = currentToken;

            match(TokenType.id_class);
            List<String> genericTypes = optionalGenerics();

            ReferenceType t = new ReferenceType(classIdToken.getLexeme());
            t.setGenericTypes(genericTypes);

            return t;
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {TokenType.reserved_word_boolean, TokenType.reserved_word_char, TokenType.reserved_word_int, TokenType.reserved_word_float, TokenType.id_class};
            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }
    }

    private Type primitiveType() throws CompilerException {
        printIfDebug("->PrimitiveType");
        //RULE: <primitive_type> ::= boolean
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_boolean})) {
            match(TokenType.reserved_word_boolean);
            return new SBoolean();
        }
        //RULE: <primitive_type> ::= char
        else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_char})) {
            match(TokenType.reserved_word_char);
            return new Char();
        }
        //RULE: <primitive_type> ::= int
        else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_int})) {
            match(TokenType.reserved_word_int);
            return new Int();
        }
        // RULE <primitive_type> ::= float
        else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_float})) {
            match(TokenType.reserved_word_float);
            return new SFloat();
        }
        else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {TokenType.reserved_word_boolean, TokenType.reserved_word_char, TokenType.reserved_word_int, TokenType.reserved_word_float};
            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }
    }

    private void attributeMethodSuccessor(Privacy currentMemberPrivacy, boolean staticity, Type type, Token declarationToken) throws CompilerException {
        printIfDebug("->AttributeMethodSuccessor");

        // RULE <attribute_method_successor> ::= <method_successor>;
        if(currentTokenIn(new TokenType[]{TokenType.punctuation_open_parenthesis})) {
            methodSuccessor(currentMemberPrivacy, staticity, type, declarationToken);
        }
        // RULE <attribute_method_successor> ::= <attribute_successor>;
        else if(currentTokenIn(new TokenType[]{TokenType.punctuation_semicolon, TokenType.punctuation_comma, TokenType.assign_normal})) {
            attributeSuccessor(currentMemberPrivacy, staticity, type, declarationToken);
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {TokenType.punctuation_semicolon, TokenType.punctuation_open_parenthesis};
            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }
    }

    private void attributeSuccessor(Privacy currentMemberPrivacy, boolean staticity, Type type, Token declarationToken) throws CompilerException {
        printIfDebug("->AttributeSuccessor");

        ConcreteClass c = SymbolTable.getInstance().getCurrentConcreteClass();

        Attribute a = new Attribute(declarationToken, c);
        a.setPrivacy(currentMemberPrivacy);
        a.setStatic(staticity);
        a.setType(type);

        c.addAttribute(a);

        // RULE <attribute_successor> ::= , <id_method_variable><attribute_successor>
        if(currentTokenIn(new TokenType[]{TokenType.punctuation_comma})) {
            match(TokenType.punctuation_comma);

            Token newDeclarationToken = currentToken;

            match(TokenType.id_method_variable);

            attributeSuccessor(currentMemberPrivacy, staticity, type, newDeclarationToken);
        }
        // RULE <attribute_successor> ::= =<composite_expression>;
        else if(currentTokenIn(new TokenType[]{TokenType.assign_normal})) {
            match(TokenType.assign_normal);
            compositeExpression();
            match(TokenType.punctuation_semicolon);
        }
        //RULE <attribute_successor> ::= ;
        else if(currentTokenIn(new TokenType[]{TokenType.punctuation_semicolon})) {
            match(TokenType.punctuation_semicolon);
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {TokenType.punctuation_semicolon, TokenType.punctuation_comma, TokenType.assign_normal};
            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }

    }

    private void methodSuccessor(Privacy currentMemberPrivacy, boolean staticity, Type type, Token declarationToken) throws CompilerException {
        printIfDebug("->MethodSuccessor");

        ConcreteClass c = SymbolTable.getInstance().getCurrentConcreteClass();

        Method m = new Method(declarationToken, c);
        m.setPrivacy(currentMemberPrivacy);
        m.setStatic(staticity);
        m.setReturnType(type);

        c.addMethod(m);

        // RULE <method_successor> ::= <formal_arguments><block>
        formalArguments();
        BlockNode block = block();

        m.setAST(block);
    }

    private void formalArguments() throws CompilerException {
        printIfDebug("->FormalArguments");
        match(TokenType.punctuation_open_parenthesis);
        optionalFormalArgumentsList();
        match(TokenType.punctuation_close_parenthesis);
    }

    private void optionalFormalArgumentsList() throws CompilerException {
        printIfDebug("->OptionalFormalArgumentsList");
        // RULE: <optional_formal_arguments_list> ::= <formal_arguments_list>
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_boolean, TokenType.reserved_word_char, TokenType.reserved_word_int, TokenType.reserved_word_float, TokenType.id_class})) {
            formalArgumentsList();
        }
        // RULE: <optional_formal_arguments_list> ::= epsilon
        // We do nothing
    }

    private void formalArgumentsList() throws CompilerException {
        printIfDebug("->FormalArgumentsList");
        formalArgument();
        formalArgumentListSuccessor();
    }

    private void formalArgument() throws CompilerException {
        printIfDebug("->FormalArgument");
        Type type = type();

        Token declarationToken = currentToken;

        match(TokenType.id_method_variable);

        Class currentClassOrInterface = SymbolTable.getInstance().getCurrentClassOrInterface();

        Parameter p = new Parameter(declarationToken, currentClassOrInterface);
        p.setType(type);
        currentClassOrInterface.getCurrentUnit().addParameter(p);
    }

    private void formalArgumentListSuccessor() throws CompilerException {
        printIfDebug("->FormalArgumentListSuccessor");
        // RULE: <formal_arguments_list_successor> ::= , <formal_arguments_list>
        if(currentTokenIn(new TokenType[]{TokenType.punctuation_comma})) {
            match(TokenType.punctuation_comma);
            formalArgumentsList();
        }
        // RULE: <formal_arguments_list_successor> ::= epsilon
        // We do nothing
    }

    private void interfaceNT() throws CompilerException {
        printIfDebug("->InterfaceNT");
        match(TokenType.reserved_word_interface);

        Token classDeclarationToken = currentToken;

        match(TokenType.id_class);

        List<String> generics = optionalGenerics();

        Interface i = new Interface(classDeclarationToken);
        i.setGenericTypes(generics);
        SymbolTable.getInstance().addInterface(i);

        optionalExtends();

        match(TokenType.punctuation_open_curly);
        headerList();
        match(TokenType.punctuation_close_curly);
    }

    private void optionalExtends() throws CompilerException {
        printIfDebug("->OptionalExtends");
        //RULE: <optional_inheritance> ::= rw_extends id_class
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_extends})) {
            match(TokenType.reserved_word_extends);

            Token extensionToken = currentToken;

            match(TokenType.id_class);

            List<String> generics =  optionalGenerics();

            Interface i = SymbolTable.getInstance().getCurrentInterface();
            i.setInheritance(extensionToken);
            i.setParentDeclaredGenericTypes(generics);
        }
        //RULE <optional_inheritance> ::= epsilon
        //We do nothing
    }

    private void headerList() throws CompilerException {
        printIfDebug("->HeaderList");
        // RULE: <header_list> ::= <method_header> <header_list>
        // static, boolean, char, int, id_class, void
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_static,
                TokenType.reserved_word_boolean,
                TokenType.reserved_word_char,
                TokenType.reserved_word_int,
                TokenType.reserved_word_float,
                TokenType.id_class,
                TokenType.reserved_word_void})) {
            methodHeader();
            headerList();
        }
        // RULE: <header_list> ::= epsilon
        // We do nothing
    }

    private void methodHeader() throws CompilerException {
        printIfDebug("->MethodHeader");

        boolean staticity = optionalStatic();
        Type type = memberType();

        Token declarationToken = currentToken;

        match(TokenType.id_method_variable);

        Interface i = SymbolTable.getInstance().getCurrentInterface();

        Method m = new Method(declarationToken, i);
        m.setPrivacy(pub);
        m.setReturnType(type);
        m.setStatic(staticity);

        i.addMethod(m);

        formalArguments();
        match(TokenType.punctuation_semicolon);
    }

    private BlockNode block() throws CompilerException {
        printIfDebug("->Block");
        Token openBlock = currentToken;

        match(TokenType.punctuation_open_curly);

        BlockNode block = new BlockNode();
        block.setToken(openBlock);

        sentenceList();
        match(TokenType.punctuation_close_curly);

        return block;
    }

    private void sentenceList() throws CompilerException {
        printIfDebug("->SentenceList");
        TokenType[] sentenceFirsts = {
                TokenType.punctuation_semicolon,
                TokenType.operand_plus,
                TokenType.operand_minus,
                TokenType.operand_not,
                TokenType.reserved_word_null,
                TokenType.reserved_word_true,
                TokenType.reserved_word_false,
                TokenType.literal_int,
                TokenType.literal_float,
                TokenType.literal_char,
                TokenType.literal_string,
                TokenType.reserved_word_this,
                TokenType.id_method_variable,
                TokenType.reserved_word_new,
                TokenType.id_class,
                TokenType.punctuation_open_parenthesis,
                TokenType.punctuation_open_curly,
                TokenType.reserved_word_var,
                TokenType.reserved_word_return,
                TokenType.reserved_word_if,
                TokenType.reserved_word_while
        };

        // RULE: <sentence_list> ::= <sentence><sentence_list>
        if(currentTokenIn(sentenceFirsts)) {
            sentence();
            sentenceList();
        }
        // RULE <sentence_list> ::= epsilon
        // We do nothing
    }

    private void sentence() throws CompilerException {
        printIfDebug("->Sentence");
        TokenType[] assignmentCallFirsts = {
                TokenType.operand_plus,
                TokenType.operand_minus,
                TokenType.operand_not,
                TokenType.reserved_word_null,
                TokenType.reserved_word_true,
                TokenType.reserved_word_false,
                TokenType.literal_int,
                TokenType.literal_float,
                TokenType.literal_char,
                TokenType.literal_string,
                TokenType.reserved_word_this,
                TokenType.id_method_variable,
                TokenType.reserved_word_new,
                TokenType.id_class,
                TokenType.punctuation_open_parenthesis
        };

        //RULE <sentence> ::= <assignment_call>
        if(currentTokenIn(assignmentCallFirsts)) {
            assignmentCall();
            match(TokenType.punctuation_semicolon);
        } //RULE <sentence> ::= ;
        else if(currentTokenIn(new TokenType[]{TokenType.punctuation_semicolon})) {
            match(TokenType.punctuation_semicolon);
        } //RULE <sentence> ::= <local_variable> ;
        else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_var})) {
            localVariable();
            match(TokenType.punctuation_semicolon);
        } //RULE <sentence> ::= <return> ;
        else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_return})) {
            returnNT();
            match(TokenType.punctuation_semicolon);
        } //RULE <sentence> ::= <if>
        else if (currentTokenIn(new TokenType[]{TokenType.reserved_word_if})) {
            ifNT();
        } //RULE <sentence> ::= <while>
        else if (currentTokenIn(new TokenType[]{TokenType.reserved_word_while})) {
            whileNT();
        } //RULE <sentence> ::= <block>
        else if (currentTokenIn(new TokenType[]{TokenType.punctuation_open_curly})) {
            block();
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {
                    TokenType.punctuation_semicolon, TokenType.operand_plus,
                    TokenType.operand_minus, TokenType.operand_not,
                    TokenType.reserved_word_null, TokenType.reserved_word_true,
                    TokenType.reserved_word_false, TokenType.literal_int, TokenType.literal_float,
                    TokenType.literal_char, TokenType.literal_string,
                    TokenType.reserved_word_this, TokenType.id_method_variable,
                    TokenType.reserved_word_new, TokenType.id_class,
                    TokenType.punctuation_open_parenthesis, TokenType.punctuation_open_curly,
                    TokenType.reserved_word_var, TokenType.reserved_word_return,
                    TokenType.reserved_word_if, TokenType.reserved_word_while
            };
            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }
    }

    private void whileNT() throws CompilerException {
        match(TokenType.reserved_word_while);
        match(TokenType.punctuation_open_parenthesis);
        expression();
        match(TokenType.punctuation_close_parenthesis);
        sentence();
    }

    private void ifNT() throws CompilerException {
        printIfDebug("->IfNT");
        match(TokenType.reserved_word_if);
        match(TokenType.punctuation_open_parenthesis);
        expression();
        match(TokenType.punctuation_close_parenthesis);
        sentence();
        optionalElse();
    }

    private void optionalElse() throws CompilerException {
        printIfDebug("->OptionalElse");
        // RULE: <optional_else> ::= else <sentence>
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_else})) {
            elseNT();
        }
        // RULE: <optional_else> ::= epsilon
        // We do nothing
    }

    private void elseNT() throws CompilerException {
        printIfDebug("->ElseNT");
        match(TokenType.reserved_word_else);
        sentence();
    }

    private void returnNT() throws CompilerException {
        printIfDebug("->ReturnNT");
        match(TokenType.reserved_word_return);
        optionalExpression();
    }

    private void localVariable() throws CompilerException {
        printIfDebug("->LocalVariable");
        match(TokenType.reserved_word_var);
        match(TokenType.id_method_variable);
        match(TokenType.assign_normal);
        compositeExpression();
    }

    private void assignmentCall() throws CompilerException {
        printIfDebug("->AssignmentCall");
        expression();
    }

    private void optionalExpression() throws CompilerException {
        printIfDebug("->OptionalExpression");
        TokenType[] expressionFirsts = {
                TokenType.operand_plus,
                TokenType.operand_minus,
                TokenType.operand_not,
                TokenType.reserved_word_null,
                TokenType.reserved_word_true,
                TokenType.reserved_word_false,
                TokenType.literal_int,
                TokenType.literal_float,
                TokenType.literal_char,
                TokenType.literal_string,
                TokenType.reserved_word_this,
                TokenType.id_method_variable,
                TokenType.reserved_word_new,
                TokenType.id_class,
                TokenType.punctuation_open_parenthesis
        };

        // RULE: <optional_expression> ::= <expression>
        if(currentTokenIn(expressionFirsts)) {
            expression();
        }

        // RULE: <optional_expression> ::= epsilon
        // We do nothing
    }

    private void expression() throws CompilerException {
        printIfDebug("->Expression");
        compositeExpression();
        expressionSuccessor();
    }

    private void compositeExpression() throws CompilerException {
        printIfDebug("->CompositeExpression");
        basicExpression();
        compositeExpressionRecursion();
    }

    private void basicExpression() throws CompilerException {
        printIfDebug("->BasicExpression");
        TokenType[] unaryOperatorFirsts = {
                TokenType.operand_plus,
                TokenType.operand_not,
                TokenType.operand_minus
        };

        TokenType[] operandFirsts = {
                TokenType.reserved_word_null,
                TokenType.reserved_word_true,
                TokenType.reserved_word_false,
                TokenType.literal_int,
                TokenType.literal_char,
                TokenType.literal_string,
                TokenType.literal_float,
                TokenType.reserved_word_this,
                TokenType.id_method_variable,
                TokenType.reserved_word_new,
                TokenType.id_class,
                TokenType.punctuation_open_parenthesis
        };

        if(currentTokenIn(unaryOperatorFirsts)) {
            unaryOperator();
            operand();
        } else if(currentTokenIn(operandFirsts)) {
            operand();
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {
                    TokenType.operand_plus, TokenType.operand_not, TokenType.operand_minus,
                    TokenType.reserved_word_null, TokenType.reserved_word_true,
                    TokenType.reserved_word_false, TokenType.literal_int,
                    TokenType.literal_char, TokenType.literal_string,
                    TokenType.literal_float, TokenType.reserved_word_this,
                    TokenType.id_method_variable, TokenType.reserved_word_new,
                    TokenType.id_class, TokenType.punctuation_open_parenthesis
            };

            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }
    }

    private void compositeExpressionRecursion() throws CompilerException {
        printIfDebug("->CompositeExpressionRecursion");
        TokenType[] binaryOperatorFirsts = {
                TokenType.operand_or,
                TokenType.operand_and,
                TokenType.operand_equals,
                TokenType.operand_not_equals,
                TokenType.operand_lesser,
                TokenType.operand_greater,
                TokenType.operand_lesser_equal,
                TokenType.operand_greater_equal,
                TokenType.operand_plus,
                TokenType.operand_minus,
                TokenType.operand_times,
                TokenType.operand_division,
                TokenType.operand_modulo
        };

        // RULE: <composite_expression_recursion> ::= <binary_operand><basic_expression><composite_expression_recursion>
        if(currentTokenIn(binaryOperatorFirsts)) {
            binaryOperator();
            basicExpression();
            compositeExpressionRecursion();
        }

        // RULE: <composite_expression_recursion> ::= epsilon
        // We do nothing
    }

    private void expressionSuccessor() throws CompilerException {
        printIfDebug("->ExpressionSuccessor");
        // RULE: <expression_successor> ::= = <expression>
        if(currentTokenIn(new TokenType[]{TokenType.assign_normal})) {
            match(TokenType.assign_normal);
            expression();
        }

        // RULE: <expression_successor> ::= epsilon
        // We do nothing
    }

    private void unaryOperator() throws CompilerException {
        printIfDebug("->UnaryOperator");
        if(currentTokenIn(new TokenType[]{TokenType.operand_plus})) {
            match(TokenType.operand_plus);
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_minus})) {
            match(TokenType.operand_minus);
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_not})) {
            match(TokenType.operand_not);
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {
                    TokenType.operand_plus,
                    TokenType.operand_not,
                    TokenType.operand_minus
            };

            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }
    }

    private void binaryOperator() throws CompilerException {
        printIfDebug("->BinaryOperator");
        if(currentTokenIn(new TokenType[]{TokenType.operand_or})) {
            match(TokenType.operand_or);
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_and})) {
            match(TokenType.operand_and);
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_equals})) {
            match(TokenType.operand_equals);
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_not_equals})) {
            match(TokenType.operand_not_equals);
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_lesser})) {
            match(TokenType.operand_lesser);
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_greater})) {
            match(TokenType.operand_greater);
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_lesser_equal})) {
            match(TokenType.operand_lesser_equal);
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_greater_equal})) {
            match(TokenType.operand_greater_equal);
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_plus})) {
            match(TokenType.operand_plus);
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_minus})) {
            match(TokenType.operand_minus);
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_times})) {
            match(TokenType.operand_times);
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_division})) {
            match(TokenType.operand_division);
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_modulo})) {
            match(TokenType.operand_modulo);
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {
                    TokenType.operand_or,
                    TokenType.operand_and,
                    TokenType.operand_equals,
                    TokenType.operand_not_equals,
                    TokenType.operand_lesser,
                    TokenType.operand_greater,
                    TokenType.operand_lesser_equal,
                    TokenType.operand_greater_equal,
                    TokenType.operand_plus,
                    TokenType.operand_minus,
                    TokenType.operand_times,
                    TokenType.operand_division,
                    TokenType.operand_modulo
            };

            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }
    }

    private void operand() throws CompilerException {
        printIfDebug("->Operand");
        TokenType[] literalFirsts = {
                TokenType.reserved_word_null,
                TokenType.reserved_word_true,
                TokenType.reserved_word_false,
                TokenType.literal_int,
                TokenType.literal_char,
                TokenType.literal_string,
                TokenType.literal_float
        };

        TokenType[] accessFirsts = {
                TokenType.reserved_word_this,
                TokenType.id_method_variable,
                TokenType.id_class,
                TokenType.reserved_word_new,
                TokenType.punctuation_open_parenthesis
        };

        if(currentTokenIn(literalFirsts)) {
            literal();
        } else if(currentTokenIn(accessFirsts)) {
            access();
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {
                    TokenType.reserved_word_null,
                    TokenType.reserved_word_true,
                    TokenType.reserved_word_false,
                    TokenType.literal_int,
                    TokenType.literal_char,
                    TokenType.literal_string,
                    TokenType.literal_float,
                    TokenType.reserved_word_this,
                    TokenType.id_method_variable,
                    TokenType.id_class,
                    TokenType.reserved_word_new,
                    TokenType.punctuation_open_parenthesis
            };

            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }

    }

    private void literal() throws CompilerException {
        printIfDebug("->Literal");
        if(currentTokenIn(new TokenType[]{TokenType.literal_int})) {
            match(TokenType.literal_int);
        } else if(currentTokenIn(new TokenType[]{TokenType.literal_char})) {
            match(TokenType.literal_char);
        } else if(currentTokenIn(new TokenType[]{TokenType.literal_string})) {
            match(TokenType.literal_string);
        } else if(currentTokenIn(new TokenType[]{TokenType.literal_float})) {
            match(TokenType.literal_float);
        } else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_true})) {
            match(TokenType.reserved_word_true);
        } else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_false})) {
            match(TokenType.reserved_word_false);
        } else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_null})) {
            match(TokenType.reserved_word_null);
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {
                    TokenType.reserved_word_null,
                    TokenType.reserved_word_true,
                    TokenType.reserved_word_false,
                    TokenType.literal_int,
                    TokenType.literal_char,
                    TokenType.literal_string,
                    TokenType.literal_float
            };

            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }
    }

    private void access() throws CompilerException {
        printIfDebug("->Access");
        primary();
        optionalChaining();
    }

    private void primary() throws CompilerException {
        printIfDebug("->Primary");
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_this})) {
            thisAccess();
        } else if(currentTokenIn(new TokenType[]{TokenType.id_method_variable})) {
            methodVariableAccess();
        } else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_new})) {
            constructorAccess();
        } else if(currentTokenIn(new TokenType[]{TokenType.id_class})) {
            staticMethodAccess();
        } else if(currentTokenIn(new TokenType[]{TokenType.punctuation_open_parenthesis})) {
            parenthesizedExpression();
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {
                    TokenType.reserved_word_this,
                    TokenType.id_method_variable,
                    TokenType.id_class,
                    TokenType.reserved_word_new,
                    TokenType.punctuation_open_parenthesis
            };

            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }
    }

    private void parenthesizedExpression() throws CompilerException {
        printIfDebug("->ParenthesizedExpression");
        match(TokenType.punctuation_open_parenthesis);
        expression();
        match(TokenType.punctuation_close_parenthesis);
    }

    private void staticMethodAccess() throws CompilerException {
        printIfDebug("->StaticMethodAccess");
        match(TokenType.id_class);
        match(TokenType.punctuation_colon);
        match(TokenType.id_method_variable);
        actualArguments();
    }

    private void constructorAccess() throws CompilerException {
        printIfDebug("->ConstructorAccess");
        match(TokenType.reserved_word_new);
        match(TokenType.id_class);
        optionalGenericsInstantiation();
        actualArguments();
    }

    private void optionalGenericsInstantiation() throws CompilerException {
        printIfDebug("->OptionalGenericsInstantiation");

        // RULE <optional_generic_instantiation> ::= < <optional_generic_types_list> >
        if(currentTokenIn(new TokenType[]{TokenType.operand_lesser})) {
            match(TokenType.operand_lesser);
            optionalGenericTypesList();
            match(TokenType.operand_greater);
        }

        // RULE <optional_generic_instantiation> ::=  epsilon
        // We do nothing
    }

    private List<String> optionalGenericTypesList() throws CompilerException {
        printIfDebug("->OptionalGenericTypesList");

        List<String> genericTypes = new ArrayList<>();

        // RULE <optional_generic_types_list> ::= <generic_types_list>
        if(currentTokenIn(new TokenType[]{TokenType.id_class})) {
            genericTypesList(genericTypes);
        }

        // RULE <optional_generic_types_list> ::= epsilon
        // We do nothing

        return genericTypes;
    }

    private void methodVariableAccess() throws CompilerException {
        printIfDebug("->MethodVariableAccess");
        match(TokenType.id_method_variable);
        methodVariableAccessSuccessor();
    }

    private void methodVariableAccessSuccessor() throws CompilerException {
        printIfDebug("->MethodVariableAccessSuccessor");
        // Rule: <method_variable_access_successor> ::= <actual_arguments>
        if(currentTokenIn(new TokenType[]{TokenType.punctuation_open_parenthesis})) {
            actualArguments();
        }

        // Rule: <method_variable_access_successor> ::= epsilon
        // We do nothing!
    }

    private void thisAccess() throws CompilerException {
        printIfDebug("->ThisAccess");
        match(TokenType.reserved_word_this);
    }

    private void actualArguments() throws CompilerException {
        printIfDebug("->ActualArguments");
        match(TokenType.punctuation_open_parenthesis);
        optionalExpressionList();
        match(TokenType.punctuation_close_parenthesis);
    }

    private void optionalExpressionList() throws CompilerException {
        printIfDebug("->OptionalExpressionList");
        TokenType[] expressionFirsts = {
                TokenType.operand_plus,
                TokenType.operand_minus,
                TokenType.operand_not,
                TokenType.reserved_word_null,
                TokenType.reserved_word_true,
                TokenType.reserved_word_false,
                TokenType.literal_int,
                TokenType.literal_float,
                TokenType.literal_char,
                TokenType.literal_string,
                TokenType.reserved_word_this,
                TokenType.id_method_variable,
                TokenType.reserved_word_new,
                TokenType.id_class,
                TokenType.punctuation_open_parenthesis
        };

        // Rule: <optional_expression_list> ::= <expression_list>
        if(currentTokenIn(expressionFirsts)) {
            expressionList();
        }

        // Rule: <optional_expression_list> ::= epsilon
        // We do nothing
    }

    private void expressionList() throws CompilerException {
        printIfDebug("->ExpressionList");
        expression();
        expressionListSuccessor();
    }

    private void expressionListSuccessor() throws CompilerException {
        printIfDebug("->ExpressionListSuccessor");
        // Rule: <expression_list_successor> ::= ,<expression_list>
        if(currentTokenIn(new TokenType[]{TokenType.punctuation_comma})) {
            match(TokenType.punctuation_comma);
            expressionList();
        }

        // Rule: <expression_list_successor> ::= epsilon
        // We do nothing
    }

    private void optionalChaining() throws CompilerException {
        printIfDebug("->OptionalChaining");
        // RULE: <optional_chaining> ::= <chaining>
        // Note: in the grammar, there is no <chaining> non-terminal symbol,
        // we add the method to have a cleaner code
        if(currentTokenIn(new TokenType[]{TokenType.punctuation_colon})) {
            chaining();
        }

        // RULE: <optional_chaining> ::= epsilon
        // We do nothing
    }

    private void chaining() throws CompilerException {
        printIfDebug("->Chaining");
        match(TokenType.punctuation_colon);
        match(TokenType.id_method_variable);
        chainingSuccessor();
    }

    private void chainingSuccessor() throws CompilerException {
        printIfDebug("->ChainingSuccessor");
        if(currentTokenIn(new TokenType[]{TokenType.punctuation_open_parenthesis})) {
            actualArguments();
            optionalChaining();
        } else {
            /*
             * Lo ponemos en el else, sin chequear primeros porque, si
             * llega a venir epsilon, se lidia con eso en optionalChaining.
             * */
            optionalChaining();
        }
    }
}
