///[Error:m|14]
class A {
    public int a;
}

class B {
    public A b;
}

class Main {
    public static void main() {}
    public void m() {
        var x = new B();
        x.b.a.m();
    }
}