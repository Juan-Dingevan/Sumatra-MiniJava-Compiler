///[Error:m|11]
class A {
    private int m() {return 3;}
}

class Main {
    public static void main() {}

    public void m() {
        var a = new A();
        var x = a.m();
    }
}