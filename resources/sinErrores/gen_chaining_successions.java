///1&1&2&2&2&2&1&1&2&2&2&2&1&1&2&2&2&2&1&1&2&2&2&2&exitosamente
class A {
    public int x;

    public int getX() {
        return x;
    }

    public A(int x) {
        this.x = x;
    }
}

class B {
    public A a;
    public int y;

    public A getA() {
        return a;
    }

    public B(A a, int y) {
        this.a = a;
        this.y = y;
    }

    public int getY() {
        return y;
    }
}

class StaticC {
    private static A a;
    private static B b;
    public static void method() {
        a = new A(2);
        b = new B(a, 1);

        var p1 = b.y;					//1
        var p2 = b.getY();				//1

        var p3 = b.a.x;					//2
        var p4 = b.a.getX();			//2

        var p5 = b.getA().x;			//2
        var p6 = b.getA().getX();		//2

        debugPrint(p1);
        debugPrint(p2);
        debugPrint(p3);
        debugPrint(p4);
        debugPrint(p5);
        debugPrint(p6);
        System.println();
    }
}

class DynamicC {
    private A a;
    private B b;

    public DynamicC(int x, int y) {
        a = new A(x);
        b = new B(a, y);
    }

    public void method() {
        var p1 = b.y;					//1
        var p2 = b.getY();				//1

        var p3 = b.a.x;					//2
        var p4 = b.a.getX();			//2

        var p5 = b.getA().x;			//2
        var p6 = b.getA().getX();		//2

        debugPrint(p1);
        debugPrint(p2);
        debugPrint(p3);
        debugPrint(p4);
        debugPrint(p5);
        debugPrint(p6);
        System.println();
    }
}

class Main {
    public static void main() {
        localVars();
        parameters(new A(2), new B(new A(2), 1));
        (new DynamicC(1, 2)).method();
        StaticC.method();
    }

    public static void localVars() {
        var a = new A(2);
        var b = new B(a, 1);

        var p1 = b.y;					//1
        var p2 = b.getY();				//1

        var p3 = b.a.x;					//2
        var p4 = b.a.getX();			//2

        var p5 = b.getA().x;			//2
        var p6 = b.getA().getX();		//2

        debugPrint(p1);
        debugPrint(p2);
        debugPrint(p3);
        debugPrint(p4);
        debugPrint(p5);
        debugPrint(p6);
        System.println();
    }
    public static void parameters(A a, B b) {
        var p1 = b.y;					//1
        var p2 = b.getY();				//1

        var p3 = b.a.x;					//2
        var p4 = b.a.getX();			//2

        var p5 = b.getA().x;			//2
        var p6 = b.getA().getX();		//2

        debugPrint(p1);
        debugPrint(p2);
        debugPrint(p3);
        debugPrint(p4);
        debugPrint(p5);
        debugPrint(p6);
        System.println();
    }
}