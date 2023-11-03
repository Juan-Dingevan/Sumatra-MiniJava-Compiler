///[SinErrores]
class A {
    void am1() {}       //expected offset 0
    void am2() {}       //expected offset 1
}

class B extends A {
    void am1() {}       //expected offset 0
    void bm1() {}       //expected offset 2
}

class C extends B {}

class D extends C {
    void dm1() {}       //3
    void dm2() {}       //4

    void am1() {}       //0
    void am2() {}       //1
    void bm1() {}       //2
}

class X extends A {
    void am2() {}       //1
    void xm1() {}       //2
}

class Main {
    public static void main() {}
}