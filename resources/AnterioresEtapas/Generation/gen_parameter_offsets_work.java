///[SinErrores]
class A {
    void m1() {}

    void m2(int x) {} //expected offset: 4

    void m3(int x, int y) {} //expected offset: x 5, y 4
}

class Main {
    public static void main() {}
}