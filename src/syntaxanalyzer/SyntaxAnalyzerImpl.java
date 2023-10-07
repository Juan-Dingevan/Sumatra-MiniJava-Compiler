package syntaxanalyzer;

import exceptions.general.CompilerException;
import exceptions.semantical.sentence.InvalidExpressionAsSentenceException;
import exceptions.syntax.InvalidTokenFoundException;
import exceptions.syntax.TokenMismatchException;
import lexicalanalizer.LexicalAnalyzer;
import symboltable.ast.chaining.ChainingNode;
import symboltable.ast.chaining.MethodChainingNode;
import symboltable.ast.chaining.VariableChainingNode;
import symboltable.ast.expressionnodes.*;
import symboltable.ast.expressionnodes.accesses.*;
import symboltable.ast.expressionnodes.binaryexpressions.booltobool.AndExpressionNode;
import symboltable.ast.expressionnodes.binaryexpressions.booltobool.OrExpressionNode;
import symboltable.ast.expressionnodes.binaryexpressions.numtobool.GreaterEqualExpressionNode;
import symboltable.ast.expressionnodes.binaryexpressions.numtobool.GreaterExpressionNode;
import symboltable.ast.expressionnodes.binaryexpressions.numtobool.LesserEqualExpressionNode;
import symboltable.ast.expressionnodes.binaryexpressions.numtobool.LesserExpressionNode;
import symboltable.ast.expressionnodes.binaryexpressions.numtonum.*;
import symboltable.ast.expressionnodes.binaryexpressions.others.AssignmentExpressionNode;
import symboltable.ast.expressionnodes.binaryexpressions.others.DiffersExpressionNode;
import symboltable.ast.expressionnodes.binaryexpressions.others.EqualsExpressionNode;
import symboltable.ast.expressionnodes.literals.*;
import symboltable.ast.expressionnodes.unaryexpressions.bool.NotExpressionNode;
import symboltable.ast.expressionnodes.unaryexpressions.num.MinusExpressionNode;
import symboltable.ast.expressionnodes.unaryexpressions.num.PlusExpressionNode;
import symboltable.ast.sentencenodes.*;
import symboltable.privacy.Privacy;
import symboltable.symbols.classes.Class;
import symboltable.symbols.members.*;
import symboltable.types.*;
import symboltable.symbols.classes.ConcreteClass;
import symboltable.symbols.classes.Interface;
import symboltable.table.SymbolTable;
import symboltable.types.Void;
import token.Token;
import token.TokenConstants;
import token.TokenType;
import utility.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static symboltable.ast.sentencenodes.SentenceNode.SEMICOLON_SENTENCE;
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
            BlockNode block = block(BlockNode.NULL_PARENT);

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
        BlockNode block = block(BlockNode.NULL_PARENT);

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

    private BlockNode block(BlockNode parent) throws CompilerException {
        printIfDebug("->Block");
        Token openBlock = currentToken;

        match(TokenType.punctuation_open_curly);

        BlockNode block = new BlockNode();
        block.setToken(openBlock);
        block.setParentBlock(parent);

        sentenceList(block);
        match(TokenType.punctuation_close_curly);

        return block;
    }

    private void sentenceList(BlockNode parent) throws CompilerException {
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
            SentenceNode s = sentence(parent);

            if(s != SEMICOLON_SENTENCE)
                parent.addSentence(s);

            sentenceList(parent);
        }
        // RULE <sentence_list> ::= epsilon
        // We do nothing
    }

    private SentenceNode sentence(BlockNode parent) throws CompilerException {
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

        SentenceNode s;

        //RULE <sentence> ::= <assignment_call>
        if(currentTokenIn(assignmentCallFirsts)) {
            s = assignmentCall(parent);
            match(TokenType.punctuation_semicolon);
        } //RULE <sentence> ::= ;
        else if(currentTokenIn(new TokenType[]{TokenType.punctuation_semicolon})) {
            s = SEMICOLON_SENTENCE;
            match(TokenType.punctuation_semicolon);
        } //RULE <sentence> ::= <local_variable> ;
        else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_var})) {
            s = localVariable(parent);
            match(TokenType.punctuation_semicolon);
        } //RULE <sentence> ::= <return> ;
        else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_return})) {
            s = returnNT(parent);
            match(TokenType.punctuation_semicolon);
        } //RULE <sentence> ::= <if>
        else if (currentTokenIn(new TokenType[]{TokenType.reserved_word_if})) {
            s = ifNT(parent);
        } //RULE <sentence> ::= <while>
        else if (currentTokenIn(new TokenType[]{TokenType.reserved_word_while})) {
            s = whileNT(parent);
        } //RULE <sentence> ::= <block>
        else if (currentTokenIn(new TokenType[]{TokenType.punctuation_open_curly})) {
            s = block(parent);
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

        return s;
    }

    private WhileNode whileNT(BlockNode parent) throws CompilerException {
        printIfDebug("->WhileNT");

        Token whileToken = currentToken;

        match(TokenType.reserved_word_while);
        match(TokenType.punctuation_open_parenthesis);
        ExpressionNode e = expression();
        match(TokenType.punctuation_close_parenthesis);
        SentenceNode s = sentence(parent);

        WhileNode w = new WhileNode();
        w.setParentBlock(parent);
        w.setToken(whileToken);
        w.setSentence(s);
        w.setExpression(e);

        return w;
    }

    private IfNode ifNT(BlockNode parent) throws CompilerException {
        printIfDebug("->IfNT");

        Token ifToken = currentToken;

        match(TokenType.reserved_word_if);
        match(TokenType.punctuation_open_parenthesis);
        ExpressionNode e = expression();
        match(TokenType.punctuation_close_parenthesis);
        SentenceNode s = sentence(parent);
        ElseNode elseNode = optionalElse(parent);

        IfNode i = new IfNode();
        i.setParentBlock(parent);
        i.setToken(ifToken);
        i.setSentence(s);
        i.setElseNode(elseNode);
        i.setExpression(e);

        return i;
    }

    private ElseNode optionalElse(BlockNode parent) throws CompilerException {
        printIfDebug("->OptionalElse");

        ElseNode en;

        // RULE: <optional_else> ::= else <sentence>
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_else})) {
            en = elseNT(parent);
        } // RULE: <optional_else> ::= epsilon
        else {
            en = ElseNode.NULL_ELSE;
        }

        return en;
    }

    private ElseNode elseNT(BlockNode parent) throws CompilerException {
        printIfDebug("->ElseNT");

        Token elseToken = currentToken;

        match(TokenType.reserved_word_else);
        SentenceNode s = sentence(parent);

        ElseNode e = new ElseNode();
        e.setParentBlock(parent);
        e.setToken(elseToken);
        e.setSentence(s);

        return e;
    }

    private ReturnNode returnNT(BlockNode parent) throws CompilerException {
        printIfDebug("->ReturnNT");

        Token returnToken = currentToken;

        match(TokenType.reserved_word_return);
        ExpressionNode e = optionalExpression();

        ReturnNode rn = new ReturnNode();
        rn.setToken(returnToken);
        rn.setExpression(e);
        rn.setParentBlock(parent);

        return rn;
    }

    private LocalVariableNode localVariable(BlockNode parent) throws CompilerException {
        printIfDebug("->LocalVariable");
        match(TokenType.reserved_word_var);

        Token declarationToken = currentToken;

        match(TokenType.id_method_variable);
        match(TokenType.assign_normal);
        ExpressionNode ex = compositeExpression();

        LocalVariableNode lvn = new LocalVariableNode();
        lvn.setToken(declarationToken);
        lvn.setParentBlock(parent);
        lvn.setExpression(ex);

        Class c = SymbolTable.getInstance().getCurrentClassOrInterface();
        Variable v = new Variable(declarationToken, c);
        parent.addLocalVariable(v);

        return lvn;
    }

    private SentenceNode assignmentCall(BlockNode parent) throws CompilerException {
        printIfDebug("->AssignmentCall");
        SentenceNode sn;
        ExpressionNode e = expression();

        if(e.isAssignment()) {
            AssignmentNode an = new AssignmentNode();
            an.setToken(e.getToken());
            an.setExpression(e);
            an.setParentBlock(parent);

            sn = an;
        } else if (e.isValidAsSentence()) {
            CallNode cn = new CallNode();
            cn.setToken(e.getToken());
            cn.setExpression(e);
            cn.setParentBlock(parent);

            sn = cn;
        } else {
            throw new InvalidExpressionAsSentenceException(e.getToken(), e);
        }

        return sn;
    }

    private ExpressionNode optionalExpression() throws CompilerException {
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

        ExpressionNode e;

        // RULE: <optional_expression> ::= <expression>
        if(currentTokenIn(expressionFirsts)) {
            e = expression();
        }
        // RULE: <optional_expression> ::= epsilon
        else {
            e = ExpressionNode.NULL_EXPRESSION;
        }

        return e;
    }

    private ExpressionNode expression() throws CompilerException {
        printIfDebug("->Expression");
        ExpressionNode possibleLHS = compositeExpression();
        ExpressionNode finalExpression = expressionSuccessor(possibleLHS);
        return finalExpression;
    }

    private ExpressionNode compositeExpression() throws CompilerException {
        printIfDebug("->CompositeExpression");
        ExpressionNode possibleLHS = basicExpression();
        ExpressionNode finalExpression = compositeExpressionRecursion(possibleLHS);
        return finalExpression;
    }

    private ExpressionNode basicExpression() throws CompilerException {
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

        ExpressionNode en;

        if(currentTokenIn(unaryOperatorFirsts)) {
            UnaryExpressionNode uen = unaryOperator();
            OperandNode on = operand();
            uen.setOperandExpression(on);

            en = uen;
        } else if(currentTokenIn(operandFirsts)) {
            en = operand();
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

        return en;
    }

    private ExpressionNode compositeExpressionRecursion(ExpressionNode possibleLHS) throws CompilerException {
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

        ExpressionNode en;

        // RULE: <composite_expression_recursion> ::= <binary_operand><basic_expression><composite_expression_recursion>
        if(currentTokenIn(binaryOperatorFirsts)) {
            BinaryExpressionNode ben = binaryOperator();
            ExpressionNode incompleteRHS = basicExpression();
            ExpressionNode finalRHS = compositeExpressionRecursion(incompleteRHS);

            ben.setLHS(possibleLHS);
            ben.setRHS(finalRHS);

            en = ben;
        }// RULE: <composite_expression_recursion> ::= epsilon
        else {
            en = possibleLHS;
        }

        return en;
    }

    private ExpressionNode expressionSuccessor(ExpressionNode possibleLHS) throws CompilerException {
        printIfDebug("->ExpressionSuccessor");

        ExpressionNode en;

        // RULE: <expression_successor> ::= = <expression>
        if(currentTokenIn(new TokenType[]{TokenType.assign_normal})) {
            Token declarationToken = currentToken;

            match(TokenType.assign_normal);
            ExpressionNode rhs = expression();

            //si anda mal, ver composite expression recursion
            BinaryExpressionNode aen = new AssignmentExpressionNode();
            aen.setToken(declarationToken);
            aen.setLHS(possibleLHS);
            aen.setRHS(rhs);

            en = aen;
        }// RULE: <expression_successor> ::= epsilon
        else {
            en = possibleLHS;
        }

        return en;
    }

    private UnaryExpressionNode unaryOperator() throws CompilerException {
        printIfDebug("->UnaryOperator");

        Token operatorToken = currentToken;
        UnaryExpressionNode uen;

        if(currentTokenIn(new TokenType[]{TokenType.operand_plus})) {
            match(TokenType.operand_plus);
            uen = new PlusExpressionNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_minus})) {
            match(TokenType.operand_minus);
            uen = new MinusExpressionNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_not})) {
            match(TokenType.operand_not);
            uen = new NotExpressionNode();
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

        uen.setToken(operatorToken);

        return uen;
    }

    private BinaryExpressionNode binaryOperator() throws CompilerException {
        printIfDebug("->BinaryOperator");

        Token operatorToken = currentToken;
        BinaryExpressionNode ben;

        if(currentTokenIn(new TokenType[]{TokenType.operand_or})) {
            match(TokenType.operand_or);
            ben = new OrExpressionNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_and})) {
            match(TokenType.operand_and);
            ben = new AndExpressionNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_equals})) {
            match(TokenType.operand_equals);
            ben = new EqualsExpressionNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_not_equals})) {
            match(TokenType.operand_not_equals);
            ben = new DiffersExpressionNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_lesser})) {
            match(TokenType.operand_lesser);
            ben = new LesserExpressionNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_greater})) {
            match(TokenType.operand_greater);
            ben = new GreaterExpressionNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_lesser_equal})) {
            match(TokenType.operand_lesser_equal);
            ben = new LesserEqualExpressionNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_greater_equal})) {
            match(TokenType.operand_greater_equal);
            ben = new GreaterEqualExpressionNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_plus})) {
            match(TokenType.operand_plus);
            ben = new AddExpressionNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_minus})) {
            match(TokenType.operand_minus);
            ben = new SubtractExpressionNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_times})) {
            match(TokenType.operand_times);
            ben = new MultiplyExpressionNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_division})) {
            match(TokenType.operand_division);
            ben = new DividesExpressionNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.operand_modulo})) {
            match(TokenType.operand_modulo);
            ben = new ModuloExpressionNode();
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

        ben.setToken(operatorToken);

        return ben;
    }

    private OperandNode operand() throws CompilerException {
        printIfDebug("->Operand");

        OperandNode op;

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
            op = literal();
        } else if(currentTokenIn(accessFirsts)) {
            op = access();
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

        return op;
    }

    private LiteralNode literal() throws CompilerException {
        printIfDebug("->Literal");

        LiteralNode ln;
        Token declarationToken = currentToken;

        if(currentTokenIn(new TokenType[]{TokenType.literal_int})) {
            match(TokenType.literal_int);
            ln = new IntLiteralNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.literal_char})) {
            match(TokenType.literal_char);
            ln = new CharLiteralNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.literal_string})) {
            match(TokenType.literal_string);
            ln = new StringLiteralNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.literal_float})) {
            match(TokenType.literal_float);
            ln = new FloatLiteralNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_true})) {
            match(TokenType.reserved_word_true);
            ln = new TrueLiteralNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_false})) {
            match(TokenType.reserved_word_false);
            ln = new FalseLiteralNode();
        } else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_null})) {
            match(TokenType.reserved_word_null);
            ln = new NullLiteralNode();
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

        ln.setToken(declarationToken);

        return ln;
    }

    private AccessNode access() throws CompilerException {
        printIfDebug("->Access");
        AccessNode an = primary();
        ChainingNode cn = optionalChaining();

        an.setChainingNode(cn);

        return an;
    }

    private AccessNode primary() throws CompilerException {
        printIfDebug("->Primary");

        AccessNode primary;

        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_this})) {
            primary = thisAccess();
        } else if(currentTokenIn(new TokenType[]{TokenType.id_method_variable})) {
            primary = methodVariableAccess();
        } else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_new})) {
            primary = constructorAccess();
        } else if(currentTokenIn(new TokenType[]{TokenType.id_class})) {
            primary = staticMethodAccess();
        } else if(currentTokenIn(new TokenType[]{TokenType.punctuation_open_parenthesis})) {
            primary = parenthesizedExpression();
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

        return primary;
    }

    private ParenthesizedExpressionAccessNode parenthesizedExpression() throws CompilerException {
        printIfDebug("->ParenthesizedExpression");
        Token openToken = currentToken;
        match(TokenType.punctuation_open_parenthesis);
        ExpressionNode en = expression();
        match(TokenType.punctuation_close_parenthesis);

        ParenthesizedExpressionAccessNode pean = new ParenthesizedExpressionAccessNode();
        pean.setToken(openToken);
        pean.setExpression(en);

        return pean;
    }

    private StaticMethodAccessNode staticMethodAccess() throws CompilerException {
        printIfDebug("->StaticMethodAccess");

        Token classToken = currentToken;

        match(TokenType.id_class);
        match(TokenType.punctuation_colon);

        Token declarationToken = currentToken;

        match(TokenType.id_method_variable);
        actualArguments();

        StaticMethodAccessNode sman = new StaticMethodAccessNode();
        sman.setToken(declarationToken);
        sman.setClassToken(classToken);

        return sman;
    }

    private ConstructorAccessNode constructorAccess() throws CompilerException {
        printIfDebug("->ConstructorAccess");

        Token declarationToken = currentToken;

        match(TokenType.reserved_word_new);

        Token classToken = currentToken;

        match(TokenType.id_class);


        optionalGenericsInstantiation();
        actualArguments();

        ConstructorAccessNode can = new ConstructorAccessNode();
        can.setToken(declarationToken);
        can.setClassToken(classToken);

        return can;
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

    private AccessNode methodVariableAccess() throws CompilerException {
        printIfDebug("->MethodVariableAccess");

        Token declarationToken = currentToken;

        match(TokenType.id_method_variable);
        AccessNode mvan = methodVariableAccessSuccessor(declarationToken);

        return mvan;
    }

    private AccessNode methodVariableAccessSuccessor(Token declarationToken) throws CompilerException {
        printIfDebug("->MethodVariableAccessSuccessor");

        AccessNode an;

        // Rule: <method_variable_access_successor> ::= <actual_arguments>
        if(currentTokenIn(new TokenType[]{TokenType.punctuation_open_parenthesis})) {
            an = new MethodAccessNode();
            an.setToken(declarationToken);

            actualArguments();
        }
        // Rule: <method_variable_access_successor> ::= epsilon
        else {
            an = new VariableAccessNode();
            an.setToken(declarationToken);
        }

        return an;
    }

    private ThisAccessNode thisAccess() throws CompilerException {
        printIfDebug("->ThisAccess");

        Token declarationToken = currentToken;

        match(TokenType.reserved_word_this);

        ThisAccessNode tan = new ThisAccessNode();
        tan.setToken(declarationToken);

        return tan;
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

    private ChainingNode optionalChaining() throws CompilerException {
        printIfDebug("->OptionalChaining");

        ChainingNode chainingNode;

        // RULE: <optional_chaining> ::= <chaining>
        // Note: in the grammar, there is no <chaining> non-terminal symbol,
        // we add the method to have a cleaner code
        if(currentTokenIn(new TokenType[]{TokenType.punctuation_colon})) {
            chainingNode = chaining();
        } else {
            chainingNode = ChainingNode.NO_CHAINING;
        }

        // RULE: <optional_chaining> ::= epsilon
        // We do nothing
        return chainingNode;
    }

    private ChainingNode chaining() throws CompilerException {
        printIfDebug("->Chaining");
        match(TokenType.punctuation_colon);

        Token declarationToken = currentToken;

        match(TokenType.id_method_variable);

        ChainingNode chainingNode = chainingSuccessor(declarationToken);

        return chainingNode;
    }

    private ChainingNode chainingSuccessor(Token declarationToken) throws CompilerException {
        printIfDebug("->ChainingSuccessor");

        ChainingNode chainingNode;

        if(currentTokenIn(new TokenType[]{TokenType.punctuation_open_parenthesis})) {
            actualArguments();
            ChainingNode chainToChain = optionalChaining();

            chainingNode = new MethodChainingNode();
            chainingNode.setToken(declarationToken);
            chainingNode.setChainingNode(chainToChain);
        } else {
            /*
             * Lo ponemos en el else, sin chequear primeros porque, si
             * llega a venir epsilon, se lidia con eso en optionalChaining.
             * */
            ChainingNode chainToChain = optionalChaining();

            chainingNode = new VariableChainingNode();
            chainingNode.setToken(declarationToken);
            chainingNode.setChainingNode(chainToChain);
        }

        return chainingNode;
    }
}
