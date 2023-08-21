package lexicalanalizer;

import exceptions.*;
import reservedwordstable.ReservedWordsTable;
import sourcemanager.SourceManager;
import token.Token;
import utility.CharacterIdentifier;
import utility.TokenType;

import java.io.IOException;

public class LexicalAnalyzerImpl implements LexicalAnalyzer {
    private static final int MAX_INT_LITERAL_LENGTH = 9;
    private String lexeme;
    private char currentChar;
    private boolean characterUpdateOnCallIsNeeded;
    private SourceManager sourceManager;

    public LexicalAnalyzerImpl(SourceManager sourceManager) {
        this.sourceManager = sourceManager;
        characterUpdateOnCallIsNeeded = true;
    }

    private void updateLexeme() {
        lexeme += currentChar;
    }

    private void updateCurrentChar() throws CompilerException {
        try {
            currentChar = sourceManager.getNextChar();
        } catch (IOException e) {
            throw new UnexpectedErrorException("reading the source file");
        }
    }
    @Override
    public Token getNextToken() throws CompilerException {
        lexeme = "";

        if(characterUpdateOnCallIsNeeded) {
            updateCurrentChar();
            characterUpdateOnCallIsNeeded = false;
        }

        return initialState();
    }

    private Token initialState() throws CompilerException {
        while(CharacterIdentifier.isWhitespace(currentChar))
            updateCurrentChar();

        if(CharacterIdentifier.isDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return literalInt();
            //Desp pasara a ser digit() y se diferenciara entre floats e ints
        } else if(CharacterIdentifier.isUpperCaseLetter(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return identifierClass();
        } else if(CharacterIdentifier.isLowerCaseLetter(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return identifierMethodVariable();
        } else if(CharacterIdentifier.isSingleQuotation(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return literalChar0();
        } else if(CharacterIdentifier.isDoubleQuotation(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return literalString0();
        } else if (CharacterIdentifier.is(currentChar, '>')) {
            updateLexeme();
            updateCurrentChar();
            return operandGreaterThan();
        } else if (CharacterIdentifier.is(currentChar, '<')) {
            updateLexeme();
            updateCurrentChar();
            return operandLesserThan();
        } else if (CharacterIdentifier.is(currentChar, '!')) {
            updateLexeme();
            updateCurrentChar();
            return operandNot();
        } else if (CharacterIdentifier.is(currentChar, '=')) {
            updateLexeme();
            updateCurrentChar();
            return assignment();
        } else if (CharacterIdentifier.is(currentChar, '+')) {
            updateLexeme();
            updateCurrentChar();
            return operandPlus();
        } else if (CharacterIdentifier.is(currentChar, '-')) {
            updateLexeme();
            updateCurrentChar();
            return operandMinus();
        } else if (CharacterIdentifier.is(currentChar, '*')) {
            updateLexeme();
            updateCurrentChar();
            return operandTimes();
        } else if (CharacterIdentifier.is(currentChar, '/')) {
            updateLexeme();
            updateCurrentChar();
            return operandDivide();
        } else if (CharacterIdentifier.is(currentChar, '&')) {
            updateLexeme();
            updateCurrentChar();
            return operandAnd0();
        } else if (CharacterIdentifier.is(currentChar, '|')) {
            updateLexeme();
            updateCurrentChar();
            return operandOr0();
        } else if (CharacterIdentifier.is(currentChar, '%')) {
            updateLexeme();
            updateCurrentChar();
            return operandModulo();
        } else if (CharacterIdentifier.is(currentChar, '(')) {
            updateLexeme();
            updateCurrentChar();
            return punctuationOpenParenthesis();
        } else if (CharacterIdentifier.is(currentChar, ')')) {
            updateLexeme();
            updateCurrentChar();
            return punctuationCloseParenthesis();
        } else if (CharacterIdentifier.is(currentChar, '{')) {
            updateLexeme();
            updateCurrentChar();
            return punctuationOpenCurly();
        } else if (CharacterIdentifier.is(currentChar, '}')) {
            updateLexeme();
            updateCurrentChar();
            return punctuationCloseCurly();
        } else if (CharacterIdentifier.is(currentChar, ',')) {
            updateLexeme();
            updateCurrentChar();
            return punctuationComma();
        } else if (CharacterIdentifier.is(currentChar, '.')) {
            updateLexeme();
            updateCurrentChar();
            return punctuationColon();
        } else if (CharacterIdentifier.is(currentChar, ';')) {
            updateLexeme();
            updateCurrentChar();
            return punctuationSemicolon();
        } else if(CharacterIdentifier.isEOF(currentChar)) {
            return endOfFile();
        }

        updateLexeme();
        characterUpdateOnCallIsNeeded = true;
        throw new InvalidCharacterException(sourceManager.getLineNumber(), sourceManager.getLineIndex(), lexeme, currentChar);
    }

    private Token literalInt() throws CompilerException {
        if(CharacterIdentifier.isDigit(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return literalInt();
        }

        if(lexeme.length() <= MAX_INT_LITERAL_LENGTH)
            return new Token(TokenType.literal_int, lexeme, sourceManager.getLineNumber());

        throw new IntegerLiteralTooLongException(sourceManager.getLineNumber(), sourceManager.getLineIndex(), lexeme, currentChar);
    }

    private Token identifierClass() throws CompilerException {
        if(CharacterIdentifier.isUpperCaseLetter(currentChar)
        || CharacterIdentifier.isLowerCaseLetter(currentChar)
        || CharacterIdentifier.isDigit(currentChar)
        || CharacterIdentifier.is(currentChar, '_')) {
            updateLexeme();
            updateCurrentChar();
            return identifierClass();
        }

        return new Token(TokenType.id_class, lexeme, sourceManager.getLineNumber());
    }
    private Token identifierMethodVariable() throws CompilerException {
        if(CharacterIdentifier.isUpperCaseLetter(currentChar)
        || CharacterIdentifier.isLowerCaseLetter(currentChar)
        || CharacterIdentifier.isDigit(currentChar)
        || CharacterIdentifier.is(currentChar, '_')) {
            updateLexeme();
            updateCurrentChar();
            return identifierMethodVariable();
        }

        TokenType identifierTokenType = ReservedWordsTable.lookUp(lexeme);
        return new Token(identifierTokenType, lexeme, sourceManager.getLineNumber());
    }

    private Token literalChar0() throws CompilerException  {
        if(CharacterIdentifier.isCharLiteralNormalCase(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return literalChar1();
        } else if (CharacterIdentifier.isBackwardsBar(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return literalChar3();
        }

        throw new IncorrectlyFormedCharLiteralException(sourceManager.getLineNumber(), sourceManager.getLineIndex(), lexeme, currentChar);
    }
    private Token literalChar1() throws CompilerException {
        if(CharacterIdentifier.isSingleQuotation(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return literalChar2();
        }
        throw new IncorrectlyFormedCharLiteralException(sourceManager.getLineNumber(), sourceManager.getLineIndex(), lexeme, currentChar);
    }
    private Token literalChar2() {
        return new Token(TokenType.literal_char, lexeme, sourceManager.getLineNumber());
    }
    private Token literalChar3() throws CompilerException {
        if(CharacterIdentifier.isCharLiteralNormalCase(currentChar)
                || CharacterIdentifier.isBackwardsBar(currentChar)
                || CharacterIdentifier.isSingleQuotation(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return literalChar1();
        }

        throw new IncorrectlyFormedCharLiteralException(sourceManager.getLineNumber(), sourceManager.getLineIndex(), lexeme, currentChar);
    }

    private Token literalString0() throws CompilerException {
        if(CharacterIdentifier.isStringLiteralNormalCase(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return literalString0();
        } else if(CharacterIdentifier.isBackwardsBar(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return literalString2();
        } else if(CharacterIdentifier.isDoubleQuotation(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return literalString1();
        }


        throw new IncorrectlyFormedStringLiteralException(sourceManager.getLineNumber(), sourceManager.getLineIndex(), lexeme, currentChar);
    }
    private Token literalString1() {
        return new Token(TokenType.literal_string, lexeme, sourceManager.getLineNumber());
    }
    private Token literalString2() throws CompilerException {
        if(CharacterIdentifier.isEscapeCharacter(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return literalString0();
        }

        throw new IncorrectlyFormedStringLiteralException(sourceManager.getLineNumber(), sourceManager.getLineIndex(), lexeme, currentChar);
    }

    private Token operandGreaterThan() throws CompilerException {
        if(CharacterIdentifier.is(currentChar, '=')) {
            updateLexeme();
            updateCurrentChar();
            return operandGreaterEquals();
        }

        return new Token(TokenType.operand_greater, lexeme, sourceManager.getLineNumber());
    }
    private Token operandGreaterEquals() {
        return new Token(TokenType.operand_greater_equal, lexeme, sourceManager.getLineNumber());
    }

    private Token operandLesserThan() throws CompilerException {
        if(CharacterIdentifier.is(currentChar, '=')) {
            updateLexeme();
            updateCurrentChar();
            return operandLesserEquals();
        }

        return new Token(TokenType.operand_lesser, lexeme, sourceManager.getLineNumber());
    }
    private Token operandLesserEquals() {
        return new Token(TokenType.operand_lesser_equal, lexeme, sourceManager.getLineNumber());
    }

    private Token operandNot() throws CompilerException {
        if(CharacterIdentifier.is(currentChar, '=')) {
            updateLexeme();
            updateCurrentChar();
            return operandNotEquals();
        }

        return new Token(TokenType.operand_not, lexeme, sourceManager.getLineNumber());
    }
    private Token operandNotEquals() {
        return new Token(TokenType.operand_not_equals, lexeme, sourceManager.getLineNumber());
    }

    private Token assignment() throws CompilerException {
        if(CharacterIdentifier.is(currentChar, '=')) {
            updateLexeme();
            updateCurrentChar();
            return operandEquals();
        }

        return new Token(TokenType.assign_normal, lexeme, sourceManager.getLineNumber());
    }
    private Token assignmentPlus(){
        return new Token(TokenType.assign_plus, lexeme, sourceManager.getLineNumber());
    }
    private Token assignmentMinus(){
        return new Token(TokenType.assign_minus, lexeme, sourceManager.getLineNumber());
    }

    private Token operandEquals() {
        return new Token(TokenType.operand_equals, lexeme, sourceManager.getLineNumber());
    }

    private Token operandPlus() throws CompilerException {
        if(CharacterIdentifier.is(currentChar, '=')) {
            updateLexeme();
            updateCurrentChar();
            return assignmentPlus();
        }

        return new Token(TokenType.operand_plus, lexeme, sourceManager.getLineNumber());
    }
    private Token operandMinus() throws CompilerException {
        if(CharacterIdentifier.is(currentChar, '=')) {
            updateLexeme();
            updateCurrentChar();
            return assignmentMinus();
        }

        return new Token(TokenType.operand_minus, lexeme, sourceManager.getLineNumber());
    }
    private Token operandTimes() {
        return new Token(TokenType.operand_times, lexeme, sourceManager.getLineNumber());
    }
    private Token operandDivide() throws CompilerException {
        if(CharacterIdentifier.is(currentChar, '/')) {
            updateCurrentChar();
            return singleLineComment();
        }

        if(CharacterIdentifier.is(currentChar, '*')) {
            updateCurrentChar();
            return multiLineComment0();
        }

        return new Token(TokenType.operand_division, lexeme, sourceManager.getLineNumber());
    }
    private Token operandModulo() {
        return new Token(TokenType.operand_modulo, lexeme, sourceManager.getLineNumber());
    }

    private Token singleLineComment() throws CompilerException {
        while(!(CharacterIdentifier.isEOL(currentChar) || CharacterIdentifier.isEOF(currentChar)))
            updateCurrentChar();

        return getNextToken();
    }

    private Token multiLineComment0() throws CompilerException {
        while(!(CharacterIdentifier.isEOF(currentChar) || CharacterIdentifier.is(currentChar, '*')))
            updateCurrentChar();

        if(CharacterIdentifier.is(currentChar, '*')) {
            updateCurrentChar();
            return multiLineComment1();
        }

        throw new MultiLineCommentLeftEmptyException(sourceManager.getLineNumber(), sourceManager.getLineIndex(), lexeme, currentChar);
    }
    private Token multiLineComment1() throws CompilerException {
        if(CharacterIdentifier.is(currentChar, '/')) {
            updateCurrentChar();
            return getNextToken();
        }

        updateCurrentChar();
        return multiLineComment0();
    }

    private Token operandAnd0() throws CompilerException {
        if(CharacterIdentifier.is(currentChar, '&')){
            updateLexeme();
            updateCurrentChar();
            return operandAnd1();
        }

        throw new IncorrectlyFormedAndOperatorException(sourceManager.getLineNumber(), sourceManager.getLineIndex(), lexeme, currentChar);
    }
    private Token operandAnd1() {
        return new Token(TokenType.operand_and, lexeme, sourceManager.getLineNumber());
    }

    private Token operandOr0() throws CompilerException {
        if(CharacterIdentifier.is(currentChar, '|')){
            updateLexeme();
            updateCurrentChar();
            return operandOr1();
        }

        throw new IncorrectlyFormedOrOperatorException(sourceManager.getLineNumber(), sourceManager.getLineIndex(), lexeme, currentChar);
    }
    private Token operandOr1() {
        return new Token(TokenType.operand_or, lexeme, sourceManager.getLineNumber());
    }

    private Token punctuationOpenParenthesis() {
        return new Token(TokenType.punctuation_open_parenthesis, lexeme, sourceManager.getLineNumber());
    }
    private Token punctuationCloseParenthesis() {
        return new Token(TokenType.punctuation_close_parenthesis, lexeme, sourceManager.getLineNumber());
    }

    private Token punctuationOpenCurly() {
        return new Token(TokenType.punctuation_open_curly, lexeme, sourceManager.getLineNumber());
    }
    private Token punctuationCloseCurly() {
        return new Token(TokenType.punctuation_close_curly, lexeme, sourceManager.getLineNumber());
    }

    private Token punctuationComma() {
        return new Token(TokenType.punctuation_comma, lexeme, sourceManager.getLineNumber());
    }
    private Token punctuationColon() {
        return new Token(TokenType.punctuation_colon, lexeme, sourceManager.getLineNumber());
    }
    private Token punctuationSemicolon() {
        return new Token(TokenType.punctuation_semicolon, lexeme, sourceManager.getLineNumber());
    }

    private Token endOfFile() {
        return new Token(TokenType.eof, lexeme, sourceManager.getLineNumber());
    }
}
