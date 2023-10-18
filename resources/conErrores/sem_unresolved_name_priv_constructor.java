///[Error:new|8]
class A {
    private A() {}
}

class B extends A {
    public void m() {
        var x = new A();
    }
}

class Main {
    public static void main() {}
}