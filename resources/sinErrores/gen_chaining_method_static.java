///1&2&exitosamente
class A {
    private static int x;

    public static void setX(int newX) {
        x = newX;
    }

    public int getX() {
        return x;
    }

    public static void printX() {
        debugPrint(x);
    }
}

class Main {
    public static void main() {
        var a = new A();

        //we check that return-giving accesses work
        //var p1 = a.getX();
        a.printX();

        //we check that non-return giving accesses work
        a.setX(2);

        //var p2 = a.x;
        a.printX();

        //debugPrint(p1);
        //debugPrint(p2);
    }
}