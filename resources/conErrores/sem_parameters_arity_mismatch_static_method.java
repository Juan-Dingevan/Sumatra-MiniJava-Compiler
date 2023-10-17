///[Error:a1|8]
class Main {
    public static void main() {
    }

    public boolean m() {
        var a = new A();
        A.a1(1);
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