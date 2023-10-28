///[Error:a2|8]
class Main {
    public static void main() {
    }

    public boolean m() {
        var a = new A();
        a.a2(1, true);
    }
}

class A {
    public static int a1(int x1, int x2) {
        return x1 + x2;
    }

    public int a2(int x1, int x2) {
        return x1 % x2;
    }
}