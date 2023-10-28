///[Error:m|10]
class A {
    private static int m() {return 3;}
}

class Main {
    public static void main() {}

    public void m() {
        var x = A.m();
    }
}