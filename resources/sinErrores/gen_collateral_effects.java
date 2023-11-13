///0&1&1&exitosamente
class C {
    private int x;
    public C() {
        this.x = 0;
    }
    public int xCollateral() {
        x = x + 1;
        return x;
    }
    public int getX() {
        return this.x;
    }
}

class Main {
    public static void main() {
        var c = new C();

        var x1 = c.getX();
        var x2 = c.xCollateral();
        var x3 = c.getX();

        debugPrint(x1);
        debugPrint(x2);
        debugPrint(x3);
    }
}