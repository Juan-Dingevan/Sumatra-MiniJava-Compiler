///[Error:I|8]
interface I {
    int m();
}

class A {
    public void m() {
        I.m();
    }
}

class Main {
    public static void main() {}
}