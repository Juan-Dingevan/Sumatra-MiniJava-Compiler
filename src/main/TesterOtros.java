package main;

import exceptions.general.CompilerException;
import org.junit.Test;
import symboltable.privacy.Privacy;
import symboltable.symbols.members.Method;
import symboltable.symbols.members.Parameter;
import symboltable.types.Char;
import symboltable.types.Int;
import symboltable.types.ReferenceType;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TesterOtros {
    @Test
    public void parameterEqualsReturnsTrueForPrimitiveTypes() {
        Parameter x1 = new Parameter(new Token(TokenType.id_method_variable, "x", 0));
        x1.setType(new Int());

        Parameter x2 = new Parameter(new Token(TokenType.id_method_variable, "x", 0));
        x2.setType(new Int());

        assertTrue(x1.equals(x2));
    }

    @Test
    public void parameterEqualsReturnsTrueForReferenceTypes() {
        Parameter x1 = new Parameter(new Token(TokenType.id_method_variable, "x", 0));
        x1.setType(new ReferenceType("String"));

        Parameter x2 = new Parameter(new Token(TokenType.id_method_variable, "x", 0));
        x2.setType(new ReferenceType("String"));

        assertTrue(x1.equals(x2));
    }

    @Test
    public void parameterEqualsReturnsFalseWhenTypesDiffer() {
        Parameter x1 = new Parameter(new Token(TokenType.id_method_variable, "x", 0));
        x1.setType(new Int());

        Parameter x2 = new Parameter(new Token(TokenType.id_method_variable, "x", 0));
        x2.setType(new Char());

        assertFalse(x1.equals(x2));
    }

    @Test
    public void sameSignatureWorksAsExpectedInMethodsWithMultipleParameters() {
        //two methods with the following signature
        //public (dynamic) int m(int x, String y)

        Parameter x1 = new Parameter(new Token(TokenType.id_method_variable, "x", 0));
        x1.setType(new Int());

        Parameter x2 = new Parameter(new Token(TokenType.id_method_variable, "x", 0));
        x2.setType(new Int());

        Parameter y1 = new Parameter(new Token(TokenType.id_method_variable, "y", 0));
        y1.setType(new ReferenceType("String"));

        Parameter y2 = new Parameter(new Token(TokenType.id_method_variable, "y", 0));
        y2.setType(new ReferenceType("String"));

        Method m1 = new Method(new Token(TokenType.id_method_variable, "m", 0));
        m1.setPrivacy(Privacy.pub);
        m1.setStatic(false);
        m1.setReturnType(new Int());

        Method m2 = new Method(new Token(TokenType.id_method_variable, "m", 0));
        m2.setPrivacy(Privacy.pub);
        m2.setStatic(false);
        m2.setReturnType(new Int());

        try {
            m1.addParameter(x1);
            m1.addParameter(y1);
            m2.addParameter(x2);
            m2.addParameter(y2);
        } catch (CompilerException e) {
            throw new RuntimeException(e);
        }

        boolean sameSignature = m1.hasSameSignature(m2);

        assertTrue(sameSignature);
    }

    @Test
    public void sameSignatureReturnsFalseWhenOrderDiffers() {
        //two methods with the following signature
        //public (dynamic) int m(int x, String y)
        //public (dynamic) int m(String y, int i)

        Parameter x1 = new Parameter(new Token(TokenType.id_method_variable, "x", 0));
        x1.setType(new Int());

        Parameter x2 = new Parameter(new Token(TokenType.id_method_variable, "x", 0));
        x2.setType(new Int());

        Parameter y1 = new Parameter(new Token(TokenType.id_method_variable, "y", 0));
        y1.setType(new ReferenceType("String"));

        Parameter y2 = new Parameter(new Token(TokenType.id_method_variable, "y", 0));
        y2.setType(new ReferenceType("String"));

        Method m1 = new Method(new Token(TokenType.id_method_variable, "m", 0));
        m1.setPrivacy(Privacy.pub);
        m1.setStatic(false);
        m1.setReturnType(new Int());

        Method m2 = new Method(new Token(TokenType.id_method_variable, "m", 0));
        m2.setPrivacy(Privacy.pub);
        m2.setStatic(false);
        m2.setReturnType(new Int());

        try {
            m1.addParameter(x1);
            m1.addParameter(y1);
            m2.addParameter(y2);
            m2.addParameter(x2);
        } catch (CompilerException e) {
            throw new RuntimeException(e);
        }

        boolean sameSignature = m1.hasSameSignature(m2);

        assertFalse(sameSignature);
    }

    @Test
    public void sameSignatureReturnsTrueWhenNamesDifferButTypeAndOrderDoNot() {
        //two methods with the following signature
        //public (dynamic) int m(int x, String y)
        //public (dynamic) int m(int i, String s)

        Parameter x1 = new Parameter(new Token(TokenType.id_method_variable, "x", 0));
        x1.setType(new Int());

        Parameter x2 = new Parameter(new Token(TokenType.id_method_variable, "i", 0));
        x2.setType(new Int());

        Parameter y1 = new Parameter(new Token(TokenType.id_method_variable, "y", 0));
        y1.setType(new ReferenceType("String"));

        Parameter y2 = new Parameter(new Token(TokenType.id_method_variable, "s", 0));
        y2.setType(new ReferenceType("String"));

        Method m1 = new Method(new Token(TokenType.id_method_variable, "m", 0));
        m1.setPrivacy(Privacy.pub);
        m1.setStatic(false);
        m1.setReturnType(new Int());

        Method m2 = new Method(new Token(TokenType.id_method_variable, "m", 0));
        m2.setPrivacy(Privacy.pub);
        m2.setStatic(false);
        m2.setReturnType(new Int());

        try {
            m1.addParameter(x1);
            m1.addParameter(y1);
            m2.addParameter(x2);
            m2.addParameter(y2);
        } catch (CompilerException e) {
            throw new RuntimeException(e);
        }

        boolean sameSignature = m1.hasSameSignature(m2);

        assertTrue(sameSignature);
    }

    @Test
    public void sameSignatureWorksAsExpectedInMethodsWithoutParameters() {
        Method m1 = new Method(new Token(TokenType.id_method_variable, "m", 0));
        m1.setPrivacy(Privacy.pub);
        m1.setStatic(false);
        m1.setReturnType(new Int());

        Method m2 = new Method(new Token(TokenType.id_method_variable, "m", 0));
        m2.setPrivacy(Privacy.pub);
        m2.setStatic(false);
        m2.setReturnType(new Int());

        boolean sameSignature = m1.hasSameSignature(m2);
        assertTrue(sameSignature);
    }

    @Test
    public void listTests() {
        List<Integer> l = new ArrayList<>();
        f(l);
        assertEquals(l.size(), 3);
    }

    public void f(List<Integer> l) {
        l.add(1);
        l.add(2);
        l.add(3);
    }
}
