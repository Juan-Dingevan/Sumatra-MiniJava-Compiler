///[SinErrores]
interface I {
    void im0();
    void im1();
}

class A implements I {
    void im0() {}
    void im1() {}
    void am0() {}
}

class B extends A {
    //also (transitively) implements I
}

interface Y extends I {
    void im0(); //"redefinition"
    void ym0();
}

class C implements Y {
    void im0() {}
    void im1() {}
    void ym0() {}
}

class D implements Y {
    void im0() {}
    void im1() {}
    void ym0() {}
}

class Main {
    public static void main() {}
}