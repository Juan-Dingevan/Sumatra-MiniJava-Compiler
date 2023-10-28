///[Error:a|8]
class A {
    private void a() {}
}

class B extends A {
    public void m() {
        a();
    }
}

class Main {
    public static void main() {}
}