///[Error:m1|3]
interface Y {
    int m1();
}

interface I extends Y {
    I getCloneOf(I i);
}

class C implements I {
    int x;
}

class Main {
    public static void main() {}
}