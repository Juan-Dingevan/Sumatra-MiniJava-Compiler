///[Error:m|9]
class A {
    void m1() {}
}

class B {
    void m() {
        var a = new A();
        a.m1().m();
    }
}

class Main {
    public static void main() {}
}