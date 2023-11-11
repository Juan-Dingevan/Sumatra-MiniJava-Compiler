///exitosamente
class A {
    public static int m1() {
        return 1;
    }

    public char m2() {
        return 'c';
    }
}

class B {
    public A a;
    public B() {
        a = new A();
    }
}

class Main {
    public static void main() {
        var b = new B();
        var a = new A();

        A.m1();
        a.m1();
        a.m2();

        b.a.m1();
        b.a.m2();
    }
}