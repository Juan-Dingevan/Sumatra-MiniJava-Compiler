///[Error:a|8]
class A {
    private int a;
}

class B extends A {
    public void m() {
        var x = a;
    }
}

class Main {
    public static void main() {}
}