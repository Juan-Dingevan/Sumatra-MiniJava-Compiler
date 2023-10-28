///[Error:>|6]
class Main {
    public static void main() {}

    public void m() {
        var x = 1 > A.m();
    }
}

class A {
    public static boolean m() {
        return true;
    }
}