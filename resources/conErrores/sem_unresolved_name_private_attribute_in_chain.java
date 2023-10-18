///[Error:a|15]
class A {
    public int m() {return 3;}
}

class B {
    private A a;
}

class Main {
    public static void main() {}

    public void m() {
        var b = new B();
        var x = b.a.m();
    }
}