///[SinErrores]
class Main {
    public static void main() {}

    public static void m2() {
        var a = new A();
        a.x = a.m();

        var b = new B();
        b.getA();
        b.getA().x = b.getA().m();
    }
}

class A {
    public int x;
    public int m() {
        return 3;
    }
}

class B {

    public A getA() {
        return new A();
    }
}