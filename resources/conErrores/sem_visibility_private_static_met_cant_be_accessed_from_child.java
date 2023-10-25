///[Error:m|8]
class A {
    private static void m() {}
}

class B extends A {
    public void m2() {
        A.m();
    }
}

class Main {
    public static void main() {}
}