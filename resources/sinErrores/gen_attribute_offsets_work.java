///[SinErrores]
class A {
    public int a1;
    public int a2;
    private int a3; // las hijas heredaran esto, pero NO podran accederlo
}

class B extends A {
    public int b1;
}

class C extends A {
    private int c1;
    private int c2;
}

class Main {
    public static void main() {}
}