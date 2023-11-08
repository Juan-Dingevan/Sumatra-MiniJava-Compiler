///exitosamente
class A {
    int getInt() {
        return 1;
    }

    void method() {
        getInt();
    }
}

class Init {
    public static void main() {
        var a = new A();
        a.method();
    }
}