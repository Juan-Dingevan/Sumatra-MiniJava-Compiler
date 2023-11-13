///1&1&1&1&2&2&true&exitosamente

class A {
    public int x;
}
class Main {
    private static int a;
    private static int b;
    private static int c;
    private static int d;
    public static void main() {
        a = b = c = d = 1;
        System.printIln(a);
        System.printIln(b);
        System.printIln(c);
        System.printIln(d);

        method();
        colateral();
    }

    public static void method() {
        var a1 = new A();
        var a2 = new A();

        a1.x = a2.x = 2;

        debugPrint(a1.x);
        debugPrint(a2.x);
    }

    public static void colateral() {
        var c = 3;
        var x = (c = 5) > 3;
        System.printBln(x);
    }
}