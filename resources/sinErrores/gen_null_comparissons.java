///true&false&false&true&exitosamente
class A {}

class B {
    public A a;
}

class Main {
    public static void main() {
        var b = new B();

        var bool1 = b.a == null;
        var bool2 = b.a != null;

        b.a = new A();

        var bool3 = b.a == null;
        var bool4 = b.a != null;

        System.printBln(bool1);
        System.printBln(bool2);
        System.printBln(bool3);
        System.printBln(bool4);
    }
}