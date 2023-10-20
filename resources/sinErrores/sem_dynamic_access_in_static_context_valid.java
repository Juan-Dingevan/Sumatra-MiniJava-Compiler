///[SinErrores]
class Main {
    public static void main() {}

    public static void m2() {
        var a = new A();
        a.x = a.m();
    }
}

class A {
    public int x;
    public int m() {
        return 3;
    }
}