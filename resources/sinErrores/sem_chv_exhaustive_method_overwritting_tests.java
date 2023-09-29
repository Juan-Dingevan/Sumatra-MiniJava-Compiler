///[SinErrores]
class Padre<A, B> {
    A a() {}
    B b() {}
    int i() {}
    float f() {}
    char c() {}
    String s() {}

    A ap(A a) {}
    B bp(B b) {}
    int ip(int i) {}
    float fp(float f) {}
    char cp(char c) {}
    String sp(String s) {}

    A app(A a, int i) {}
    B bpp(B b, int i) {}
    int ipp(int i, A a) {}
    float fpp(float f, B b) {}
    char cpp(char c, A a, B b) {}
    String spp(String s, int i) {}

    Padre<A, B> copy() {}
    void clone(Padre<A, B> c) {}
}

class Hija<E> extends Padre<E, Object> {
    E a() {}
    Object b() {}
    int i() {}
    float f() {}
    char c() {}
    String s() {}

    E ap(E a) {}
    Object bp(Object b) {}
    int ip(int i) {}
    float fp(float f) {}
    char cp(char c) {}
    String sp(String s) {}

    E app(E a, int i) {}
    Object bpp(Object b, int i) {}
    int ipp(int i, E a) {}
    float fpp(float f, Object b) {}
    char cpp(char c, E a, Object b) {}
    String spp(String s, int i) {}
}

class Main {
    public static void main() {}
}