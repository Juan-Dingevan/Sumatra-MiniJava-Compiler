///true&exitosamente
class C {
    private int x;
    public C() {
        this.x = 0;
    }
    public int xCollateral() {
        x = x + 1;
        return x;
    }
}

class Main {
    public static void main() {
        var c = new C();

        var x1 = true || (c.xCollateral() < 1);

        System.printBln(x1);
    }
}