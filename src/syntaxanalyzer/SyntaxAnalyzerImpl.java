package syntaxanalyzer;

import exceptions.general.CompilerException;
import exceptions.syntax.InvalidTokenFoundException;
import exceptions.syntax.SyntaxException;
import exceptions.syntax.TokenMismatchException;
import lexicalanalizer.LexicalAnalyzer;
import token.Token;
import token.TokenType;

import java.util.Arrays;

public class SyntaxAnalyzerImpl implements SyntaxAnalyzer{
    private final LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;

    public SyntaxAnalyzerImpl(LexicalAnalyzer lexicalAnalyzer) {
        this.lexicalAnalyzer = lexicalAnalyzer;
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
        classList();
        match(TokenType.eof);
    }

    private void classList() throws CompilerException {
        //RULE: <class_list> ::= <class> <class_list>
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_class, TokenType.reserved_word_interface})) {
            classNT();
            classList();
        }
        //RULE: <class_list> ::= epsilon
        //We do nothing
    }

    private void classNT() throws CompilerException {
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
        match(TokenType.reserved_word_class);
        match(TokenType.id_class);
        optionalInheritance();
        match(TokenType.punctuation_open_curly);
        memberList();
        match(TokenType.punctuation_close_curly);
    }

    private void optionalInheritance() throws CompilerException {
        //RULE: <optional_inheritance> ::= <inherits_from>
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_extends})) {
            inheritsFrom();
        }
        //RULE <optional_inheritance> ::= <implements>
        else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_implements})) {
            implementsNT();
        }
        //RULE <optional_inheritance> ::= epsilon
        //We do nothing.
    }
    private void inheritsFrom() throws CompilerException {
        match(TokenType.reserved_word_extends);
        match(TokenType.id_class);
    }

    private void implementsNT() throws CompilerException {
        match(TokenType.reserved_word_implements);
        match(TokenType.id_class);
    }

    private void memberList() throws CompilerException {
        //RULE: <member_list> ::= <member><member_list>
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_static, TokenType.reserved_word_boolean,
                TokenType.reserved_word_char, TokenType.reserved_word_int, TokenType.id_class,
                TokenType.reserved_word_void, TokenType.reserved_word_public})) {

            member();
            memberList();
        }
        //RULE: <member_list> ::= epsilon
        //Do nothing
    }

    private void member() throws CompilerException {
        //RULE: <member_list> ::= <optional_static><member_type> idmetvar <metvar_successor>
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_static, TokenType.reserved_word_boolean,
                TokenType.reserved_word_char, TokenType.reserved_word_int, TokenType.id_class, TokenType.reserved_word_void})) {

            optionalStatic();
            memberType();
            match(TokenType.id_method_variable);
            attributeMethodSuccessor();
        }
        //RULE: <member_list> ::= <constructor>
        else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_public}))  {
            constructorNT();
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {TokenType.reserved_word_static, TokenType.reserved_word_boolean,
                                       TokenType.reserved_word_char, TokenType.reserved_word_int,
                                       TokenType.id_class, TokenType.reserved_word_void,
                                       TokenType.reserved_word_public};
            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }

    }
    private void optionalStatic() throws CompilerException {
        //RULE: <optional_static> ::= static
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_static})){
            match(TokenType.reserved_word_static);
        }

        //RULE: <optional_static> ::= epsilon
        //We do nothing!
    }

    private void memberType() throws CompilerException {
        //RULE: <member_type> ::= <type>
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_boolean, TokenType.reserved_word_char, TokenType.reserved_word_int, TokenType.id_class})) {
            type();
        }
        //RULE: <member_type> ::= void
        else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_void})) {
            match(TokenType.reserved_word_void);
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {TokenType.reserved_word_boolean, TokenType.reserved_word_char, TokenType.reserved_word_int, TokenType.id_class, TokenType.reserved_word_void};
            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }
    }

    private void type() throws CompilerException {
        //RULE: <type> ::= <primitive_type>
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_boolean, TokenType.reserved_word_char, TokenType.reserved_word_int})) {
            primitiveType();
        } //RULE: <type> ::= id_class
        else if(currentTokenIn(new TokenType[]{TokenType.id_class})) {
            match(TokenType.id_class);
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {TokenType.reserved_word_boolean, TokenType.reserved_word_char, TokenType.reserved_word_int, TokenType.id_class};
            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }
    }

    private void primitiveType() throws CompilerException {
        //RULE: <primitive_type> ::= boolean
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_boolean})) {
            match(TokenType.reserved_word_boolean);
        }
        //RULE: <primitive_type> ::= char
        else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_char})) {
            match(TokenType.reserved_word_char);
        }
        //RULE: <primitive_type> ::= int
        else if(currentTokenIn(new TokenType[]{TokenType.reserved_word_int})) {
            match(TokenType.reserved_word_int);
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {TokenType.reserved_word_boolean, TokenType.reserved_word_char, TokenType.reserved_word_int};
            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }
    }

    private void attributeMethodSuccessor() throws CompilerException {
        //RULE <attribute_method_successor> ::= ;
        if(currentTokenIn(new TokenType[]{TokenType.punctuation_semicolon})) {
            match(TokenType.punctuation_semicolon);
        }
        //RULE <attribute_method_successor> ::= <ArgsFormales> <Bloque>
        else if(currentTokenIn(new TokenType[]{TokenType.punctuation_open_parenthesis})) {
            formalArguments();
            block();
        } else {
            int line = currentToken.getLineNumber();
            String lexeme = currentToken.getLexeme();
            TokenType[] validTokens = {TokenType.punctuation_semicolon, TokenType.punctuation_open_parenthesis};
            throw new InvalidTokenFoundException(line, lexeme, validTokens);
        }
    }

    private void formalArguments() throws CompilerException {
        match(TokenType.punctuation_open_parenthesis);
        optionalFormalArgumentsList();
        match(TokenType.punctuation_close_parenthesis);
    }

    private void optionalFormalArgumentsList() throws CompilerException {
        // RULE: <optional_formal_arguments_list> ::= <formal_arguments_list>
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_boolean, TokenType.reserved_word_char, TokenType.reserved_word_int, TokenType.id_class})) {
            formalArgumentsList();
        }
        // RULE: <optional_formal_arguments_list> ::= epsilon
        // We do nothing
    }

    private void formalArgumentsList() throws CompilerException {
        formalArgument();
        formalArgumentListSuccessor();
    }

    private void formalArgument() throws CompilerException {
        type();
        match(TokenType.id_method_variable);
    }

    private void formalArgumentListSuccessor() throws CompilerException {
        // RULE: <formal_arguments_list_successor> ::= , <formal_arguments_list>
        if(currentTokenIn(new TokenType[]{TokenType.punctuation_comma})) {
            match(TokenType.punctuation_comma);
            formalArgumentsList();
        }
        // RULE: <formal_arguments_list_successor> ::= epsilon
        // We do nothing
    }

    private void constructorNT() throws CompilerException {
        match(TokenType.reserved_word_public);
        match(TokenType.id_class);
        formalArguments();
        block();
    }

    private void interfaceNT() throws CompilerException {
        match(TokenType.reserved_word_interface);
        match(TokenType.id_class);
        optionalExtends();
        match(TokenType.punctuation_open_curly);
        headerList();
        match(TokenType.punctuation_close_curly);
    }

    private void optionalExtends() throws CompilerException {
        //RULE: <optional_inheritance> ::= rw_extends id_class
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_extends})) {
            match(TokenType.reserved_word_extends);
            match(TokenType.id_class);
        }
        //RULE <optional_inheritance> ::= epsilon
        //We do nothing
    }

    private void headerList() throws CompilerException {
        // RULE: <header_list> ::= <method_header> <header_list>
        // static, boolean, char, int, id_class, void
        if(currentTokenIn(new TokenType[]{TokenType.reserved_word_static, TokenType.reserved_word_boolean, TokenType.reserved_word_char, TokenType.reserved_word_int, TokenType.id_class, TokenType.reserved_word_void})) {
            methodHeader();
            headerList();
        }
        // RULE: <header_list> ::= epsilon
        // We do nothing
    }

    private void methodHeader() throws CompilerException {
        optionalStatic();
        memberType();
        match(TokenType.id_method_variable);
        formalArguments();
        match(TokenType.punctuation_semicolon);
    }

    private void block() throws CompilerException {
        match(TokenType.punctuation_open_curly);
        match(TokenType.punctuation_close_curly);
    }
}
