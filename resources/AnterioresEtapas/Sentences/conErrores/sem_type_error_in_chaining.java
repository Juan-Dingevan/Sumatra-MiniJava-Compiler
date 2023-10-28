///[Error:&&|22]
class Main {
    public static void main() {}
}

class A {
    public int i;
}

class B {
    public A a;
}

class C {
    private B bb;
    public B b() {return bb;}
}

class D {
    public void m() {
        var x = new C();
        var e = true && x.b().a.i;
    }
}